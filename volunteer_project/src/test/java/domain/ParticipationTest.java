package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SystemDateTime;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipationTest{

    Volunteer volunteer ;
    Address address;
    Participation participation;
    LocalDateTime now =LocalDateTime.now();
    Action action;

    @BeforeEach
    void setUp() {
        SystemDateTime.setStub(now);
        address = new Address() ;
        address.setStreet("Iras");
        address.setStreetNumber(38);
        address.setCity("Athens");
        address.setPostalCode("11146");

        // Create a volunteer
        volunteer = new Volunteer();
        volunteer.setUsername("elenol_88");
        volunteer.setPassword("Password123!");
        volunteer.setFirstName("Eleni");
        volunteer.setLastName("Grigori");
        volunteer.setEmail("eleni@test.com");
        volunteer.setMobile("685212458");
        volunteer.setAddress(address);

        action = new Action();
        action.setTitle("New Action");
        action.setActionDescription("This is an action");


        participation = new Participation();
        participation.setAction(new Action());
    }



    @Test
    void shouldThrowExceptionWhenSettingNullAction() throws Exception {
        DomainException exception = assertThrows(DomainException.class, () -> {
            participation.setAction(null);
        });

        assertEquals("H Δράση δεν μπορεί να είναι κενή.", exception.getMessage());

    }
    @Test
    void shouldThrowExceptionWhenSettingNullVolunteer() throws Exception {
        DomainException exception = assertThrows(DomainException.class, () -> {
            participation.setVolunteer(null);
        });

        assertEquals("Πρέπει να υπάρχει εθελοντής", exception.getMessage());

    }


}
