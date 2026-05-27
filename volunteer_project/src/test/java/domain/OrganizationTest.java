package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrganizationTest {


    private Address address;
    private Organization organization;

    @BeforeEach
    public void setup() {
        address = new Address("testStreet", 55, "12345", "cityNameTest");
        organization = new Organization("myOrganization", "pAssword123!", "myOrganization@gmail.com", "6980000000", address, "myOrganizationName", "123456789", "organization description", "organization action", 1990);

    }

    @Test
    public void testOrganizationConstructor() {
        assertNotNull(organization);
        assertEquals("myOrganization", organization.getUsername());
        assertEquals("pAssword123!", organization.getPassword());
        assertEquals("myOrganization@gmail.com", organization.getEmail());
        assertEquals("6980000000", organization.getMobile());
        assertEquals(address, organization.getAddress());
        assertEquals("myOrganizationName", organization.getOrganizationName());
        assertEquals("123456789", organization.getAfm());
        assertEquals("organization description", organization.getDescriptionOfOrganization());
        assertEquals("organization action", organization.getDescriptionOfAction());
        assertEquals(1990, organization.getYearOfEstablishment());

    }

    @Test
    public void testEmptyOrganizationConstructor() {
        Organization emptyOrganization = new Organization();
        assertNull(emptyOrganization.getUsername());
        assertNull(emptyOrganization.getPassword());
        assertNull(emptyOrganization.getMobile());
        assertNull(emptyOrganization.getEmail());
        assertNull(emptyOrganization.getAddress());
        assertNull(emptyOrganization.getOrganizationName());
        assertNull(emptyOrganization.getAfm());
        assertNull(emptyOrganization.getDescriptionOfOrganization());
        assertNull(emptyOrganization.getDescriptionOfAction());
        assertNull(emptyOrganization.getYearOfEstablishment());
    }

    @Test
    public void testSettersAndGetters() {

        organization.setOrganizationName("myOrganizationUpdatedName");
        organization.setAfm("555555555");
        organization.setDescriptionOfOrganization("new description of Organization");
        organization.setDescriptionOfAction("new description of action");
        organization.setYearOfEstablishment(1999);
        Address newAddress = new Address("streetName", 5, "12345", "athens");
        organization.setAddress(newAddress);

            assertEquals("myOrganizationUpdatedName", organization.getOrganizationName());
            assertEquals("555555555", organization.getAfm());
            assertEquals("new description of Organization", organization.getDescriptionOfOrganization());
            assertEquals("new description of action", organization.getDescriptionOfAction());
            assertEquals(1999, organization.getYearOfEstablishment());
    }

    @Test
    public void testEquals() {
        Organization organization1 = new Organization("myOrganization", "pAssword123!", "myOrganization@gmail.com", "6980000000", address, "myOrganizationName", "123456789", "Organization description", "Organization action", 1990);
        Organization organization2 = new Organization("myOrganization2", "p@ssword22!", "myOrganization2@gmail.com", "6980000000", address, "myOrganizationName2", "0987654323", "Organization description", "Organization action", 1995);

       assertNotSame(organization1, organization2);
       assertNotSame(organization, organization1);
       assertNotEquals(organization1, organization2);
       assertEquals(organization, organization1);
    }

    @Test
    public void testHashCode() {
        Organization organization1 = new Organization("myOrganization", "pAssword123!", "myOrganization@gmail.com", "6980000000", address, "myOrganizationName", "123456789", "Organization description", "Organization action", 1990);
        Organization organization2 = new Organization("myOrganization2", "p@ssword22!", "myOrganization2@gmail.com", "6980000000", address, "myOrganizationName2", "0987654323", "Organization description", "Organization action", 1995);


        assertEquals(organization.hashCode(), organization1.hashCode());
        assertNotEquals(organization1.hashCode(), organization2.hashCode());
    }
}



