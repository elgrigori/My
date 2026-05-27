package persistence;

import static org.junit.jupiter.api.Assertions.*;
import domain.*;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserJPATest extends JPATest {

    private Address testAddress;
    private Validator validator;

    @BeforeEach
    public void createData() {

        testAddress = new Address("testStreet", 55, "12345", "thessaloniki");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void listOfUsers() {
        List<User> users = em.createQuery("select u from User u").getResultList();
        assertEquals(3, users.size());
        System.out.println("Users List:");
        for (User user : users) {
            System.out.println(user.getUsername());
            assertNotNull(user.getUsername());
            assertNotNull(user.getPassword());
            assertNotNull(user.getMobile());
            assertNotNull(user.getAddress());
            assertNotNull(user.getEmail());
        }
    }

    @Test
    public void listOfVolunteers() {
        List<Volunteer> volunteers = em.createQuery("SELECT v FROM Volunteer v").getResultList();
        assertEquals(2, volunteers.size());
        System.out.println("Volunteers List:");
        for (Volunteer volunteer : volunteers) {
            System.out.println(volunteer.getUsername());
            assertNotNull(volunteer.getFirstName());
            assertNotNull(volunteer.getLastName());
        }
    }


    @Test
    public void listOfCompanies() {
        List<Organization> organizations = em.createQuery("SELECT o FROM Organization o").getResultList();
        assertEquals(1, organizations.size());
        System.out.println("Companies List:");
        for (Organization organization : organizations) {
            System.out.println(organization.getUsername());
            assertNotNull(organization.getOrganizationName());
            assertNotNull(organization.getAfm());
            assertNotNull(organization.getDescriptionOfOrganization());
            assertNotNull(organization.getDescriptionOfAction());
            assertNotNull(organization.getYearOfEstablishment());
        }
    }

    @Test
    public void denyDuplicateUsers() {
        List<Volunteer> volunteers = em.createQuery("select v from Volunteer v").getResultList();
        Volunteer firstVolunteer = volunteers.getFirst();
        Volunteer duplicatedVolunteer = new Volunteer(firstVolunteer.getUsername(), firstVolunteer.getPassword(), firstVolunteer.getEmail(), firstVolunteer.getAddress(), firstVolunteer.getMobile(), firstVolunteer.getFirstName(), firstVolunteer.getLastName());

        List<Organization> organizations = em.createQuery("select o from Organization o").getResultList();
        Organization lastOrganization = organizations.getLast();
        User duplicatedCompany = new Organization(lastOrganization.getUsername(), lastOrganization.getPassword(), lastOrganization.getEmail(), lastOrganization.getMobile(), lastOrganization.getAddress(), lastOrganization.getOrganizationName(), lastOrganization.getAfm(), lastOrganization.getDescriptionOfOrganization(), lastOrganization.getDescriptionOfAction(), lastOrganization.getYearOfEstablishment());

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        assertThrows(PersistenceException.class, () -> {
            em.persist(duplicatedVolunteer);
            em.persist(duplicatedCompany);
            tx.commit();
        });

        em.close();
    }

    @Test
    public void testDuplicateUsername() {

        Volunteer volunteerTest1 = new Volunteer("volunteertest", "passWord1!", "volunteertest1@gmail.com", testAddress, "6970000000", "firstname1", "lastname1");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(volunteerTest1);
        tx.commit();

        //testUser2 with the same username with testUser1
        Volunteer volunteerTest2 = new Volunteer("volunteertest", "passWord2!", "volunteertest2@gmail.com", testAddress, "6971111111", "firstname2", "lastname2");


        // Throw error unique constraint violation due to username duplication
        tx.begin();
        assertThrows(PersistenceException.class, () -> {
            em.persist(volunteerTest2);
            tx.commit();
        });
    }


        @Test
        public void testDuplicateEmail() {

            Organization organizationTest1 = new Organization("companyTest1username", "companyPass11!", "companyEmailTest@outlook.com", "6945609978",testAddress, "companyName1" , "987678999", "description of company", "Action action action", 2010);

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(organizationTest1);
            tx.commit();

            //company2 with the same email with company1
            Organization organizationTest2 = new Organization( "companyTest2username", "companyPass22!", "companyEmailTest@outlook.com", "6937654980",testAddress, "companyName2" , "543543299", "description of company", "Action action action", 1997);

            // Throw error unique constraint violation due to email duplication
            tx.begin();
            assertThrows(PersistenceException.class, () -> {
                em.persist(organizationTest2);
                tx.commit();
            });
        }


    @Test
    public void testUserIdAndHashCode() {

        Organization organizationTest2 = new Organization("companyTestusername00", "password123!@@G", "companyEmailTest00@outlook.com", "6981230067",testAddress, "testCompany" , "987678999", "description of company", "Action action action", 2010);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(organizationTest2);
        tx.commit();

        assertNotNull(organizationTest2.getId());
        int expectedHashCode = Objects.hash(organizationTest2.getOrganizationName(), organizationTest2.getAfm());
        assertEquals(expectedHashCode, organizationTest2.hashCode());

    }

    @Test
    public void testInvalidEmail() {
        Volunteer volunteerTest = new Volunteer("volunteer1", "volunteer1!Pass", "wrongEmail", testAddress, "6978675689", "Mary", "Papadopoulou");

        Set<ConstraintViolation<User>> violations = validator.validate(volunteerTest);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
        System.out.println("violation List:");
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation);
        }
    }

    @Test
    public void testInvalidMobile() {
        Volunteer volunteerTest = new Volunteer("volunteer1", "volunteer1!Pass", "email@gmail.com", testAddress, "697867", "Mary", "Papadopoulou");

        Set<ConstraintViolation<User>> violations = validator.validate(volunteerTest);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid mobile number format", violations.iterator().next().getMessage());
        System.out.println("violation List:");
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation);
        }
    }

    @Test
    public void testInvalidPassword() {
        Volunteer volunteerTest = new Volunteer("volunteer1", "pass123", "email@gmail.com", testAddress, "6978671234", "Mary", "Papadopoulou");

        Set<ConstraintViolation<User>> violations = validator.validate(volunteerTest);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character", violations.iterator().next().getMessage());
        System.out.println("violation List:");
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation);
        }
    }

    @Test
    public void testInvalidAfm() {
        Organization organizationTest = new Organization("comapanyuserName", "pass123!C", "companyemail@gmail.com", "6978671234",testAddress, "companyNamee" , "123", "desc of company", "desc of action", 1980);
        Set<ConstraintViolation<User>> violations = validator.validate(organizationTest);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid AFM. It must be exactly 9 digits.", violations.iterator().next().getMessage());
        System.out.println("violation List:");
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation);
        }
    }

}

