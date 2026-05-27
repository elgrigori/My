package domain;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SystemDateTime;


import static org.junit.jupiter.api.Assertions.*;
public class ActivismParticipationTest {
    Volunteer volunteer ;
    Address address;
    ActivismAction action;
    LocalDateTime now =LocalDateTime.now();
    ActivismParticipation participation;

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

        action = new ActivismAction();
        action.setTitle("New Action");
        action.setActionDescription("This is an action");
        action.setLocation("Central Park");
        action.setStartAt(now.plusDays(1));
        action.setEndAt(now.plusDays(2));



        participation = new ActivismParticipation();
        participation.setParticipationStatus(ParticipationStatus.ACCEPTED);
        participation.setAction(action);
        participation.setVolunteer(volunteer);
        participation.setCreatedAt(now);

    }

    @Test
    public void allowActivismParticipationConstructor() {
        ActivismParticipation activismParticipation = new ActivismParticipation(
                ParticipationStatus.ACCEPTED, volunteer, action
        );

        // Validate fields
        assertEquals(activismParticipation.getParticipationStatus(),ParticipationStatus.ACCEPTED);
        assertEquals(activismParticipation.getVolunteer(),volunteer);
        assertEquals(activismParticipation.getCreatedAt(),now);

    }
    @Test
    public void testActivismParticipationConstructor() {
        assertNotNull(participation);
        assertEquals(ParticipationStatus.ACCEPTED, participation.getParticipationStatus());
        assertEquals(volunteer,participation.getVolunteer());
        assertEquals(action, participation.getAction());

    }
    @Test
    public void denyActivismParticipationConstructor() throws Exception{
        assertThrows(DomainException.class, () -> {
            ActivismParticipation activismParticipation = new ActivismParticipation(
                    ParticipationStatus.ACCEPTED, null, action
            );

        });
        assertThrows(DomainException.class, () -> {
            ActivismParticipation activismParticipation = new ActivismParticipation(
                    ParticipationStatus.ACCEPTED, volunteer   , null
            );

        });


    }
}
