package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VolunteerTest {

        private Address address;
        private Volunteer volunteer;

        @BeforeEach
        public void setup() {
            address = new Address("testStreet", 55, "12345", "cityNameTest");
            volunteer = new Volunteer("volunteerUsername", "password123!", "example@gmail.com", address, "6970000000", "volunteerFirstName", "volunteerLastName");
        }

        @Test
        public void testVolunteerConstructor() {
            assertNotNull(volunteer);
            assertEquals("volunteerUsername", volunteer.getUsername());
            assertEquals("password123!", volunteer.getPassword());
            assertEquals("example@gmail.com", volunteer.getEmail());
            assertEquals("6970000000", volunteer.getMobile());
            assertEquals("volunteerFirstName", volunteer.getFirstName());
            assertEquals("volunteerLastName", volunteer.getLastName());
            assertEquals(address, volunteer.getAddress());
        }

    @Test
    public void testEmptyVolunteerConstructor() {
        Volunteer volunteer1 = new Volunteer();
        assertNull(volunteer1.getUsername());
        assertNull(volunteer1.getPassword());
        assertNull(volunteer1.getFirstName());
        assertNull(volunteer1.getLastName());
        assertNull(volunteer1.getMobile());
        assertNull(volunteer1.getEmail());
    }

        @Test
        public void testSettersAndGetters() {
            volunteer.setUsername("updatedUsername");
            volunteer.setPassword("newPassword123!");
            volunteer.setEmail("volunteertest@gmail.com");
            volunteer.setMobile("6970987654");
            volunteer.setFirstName("newFirstName");
            volunteer.setLastName("newLastName");

            assertEquals("updatedUsername", volunteer.getUsername());
            assertEquals("newPassword123!", volunteer.getPassword());
            assertEquals("volunteertest@gmail.com", volunteer.getEmail());
            assertEquals("6970987654", volunteer.getMobile());
            assertEquals("newFirstName", volunteer.getFirstName());
            assertEquals("newLastName", volunteer.getLastName());
        }

        @Test
        public void testEquals() {

            Address address1 = new Address("ermou", 11, "12345", "athens");
            Volunteer user1 = new Volunteer("volunteer1", "volunteer1pass", "volunteer1@gmail.com",address1, "6978675689", "Mary", "Papadopoulou");

            Address address2 = new Address("thessalonikis", 12, "11852", "athens");
            Volunteer user2 = new Volunteer("volunteer2", "volunteer2password", "volunteer2@gmail.com", address2, "6971234567", "JOhn", "Papazoglou");

            Address address3 = new Address("thessalonikis", 12, "12345", "athens");
            Volunteer user3 = new Volunteer("volunteer2", "volunteer2password", "volunteer2@gmail.com", address3, "6971234567","JOhn", "Papazoglou");


            assertNotEquals(user1, user2);
            assertEquals(user2, user3);
        }

        @Test
        public void testHashCode() {

            Volunteer user1 = new Volunteer("volunteer1", "volunteer1pass", "volunteer1@gmail.com",address, "6978675689", "Mary", "Papadopoulou");
            Volunteer user2 = new Volunteer("volunteer1", "volunteer1pass", "volunteer1@gmail.com", address, "6978675689", "Mary", "Papadopoulou");

            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        public void testEmbeddedAddress() {

            assertNotNull(volunteer.getAddress());
            assertEquals("testStreet", volunteer.getAddress().getStreet());
            assertEquals(55, volunteer.getAddress().getStreetNumber());
            assertEquals("12345", volunteer.getAddress().getPostalCode());
            assertEquals("cityNameTest", volunteer.getAddress().getCity());
        }
    }




