package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ActivismActionTest {

    private ActivismAction action;
    LocalDateTime now =LocalDateTime.now();
    Organization organization;

    @BeforeEach
    public void setup() {
        Address address3 = new Address("pathsiwn", 26, "09876", "athens");
        organization = new Organization("testetstCompany", "company1Password!", "company1Test@outlook.com", "6945679978", address3, "companyTest" , "435346787", "Our company related with sustainable clothes", "Company's action is related to design and manufacture friendly environmental clothes", 2010);

        action = new ActivismAction("Clean the Park", "Environmental activity", LocalDateTime.of(2023, 6, 3, 12, 0) ,LocalDateTime.of(2023, 6, 3, 16, 0) ,"City Park", 4, 6, organization);
    }

//    @Test
//    void completeActionWhenParticipantsReachMinLimit() {
//
//            for(int i = 1; i < action.getTotalParticipants(); i++  )
//            {
//                action.addParticipant();
//            }
//
//            System.out.println("Collected Participants: " + action.getCollectedParticipants());
//            System.out.println("Expected Status: COMPLETED, Actual Status: " + action.getActioStatus());
//
//            assertEquals(ActionStatus.COMPLETED, action.getActioStatus());
//    }

    @Test
    void notCompleteActionBeforeReachingMinLimit() throws DomainException {
        action.addParticipant();
        action.addParticipant();


        System.out.println("Collected Participants: " + action.getCollectedParticipants());
        System.out.println("Expected Status: OPEN, Actual Status: " + action.getActioStatus());

        assertEquals(ActionStatus.OPEN, action.getActioStatus());
    }

    @Test
    void cancelActionWhenDeadlinePassesWithoutEnoughParticipants() throws DomainException {
        assertThrows(DomainException.class, () -> {

            action.setEndAt(java.time.LocalDateTime.now().minusDays(1));

            action.cancelAction();
        });

    }

//    @Test
//    void notCancelActionIfDeadlineNotPassed() {
//        assertThrows(DomainException.class, () -> {
//
//            action.setEndAt(java.time.LocalDateTime.now().plusDays(1));
//
//            action.checkAndCancelAction();
//        });
//
//    }

    @Test
    void testConstructor() {
        // 🔹 Δοκιμή με σωστές τιμές
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);

        ActivismAction action = new ActivismAction(
                "Beach Cleanup",
                "Cleaning plastic waste",
                start,
                end,
                "Miami Beach",
                5,
                10,
                organization
        );

        // 🔹 Ελέγχουμε αν αποθηκεύτηκαν σωστά οι τιμές
        assertEquals("Beach Cleanup", action.getTitle());
        assertEquals("Cleaning plastic waste", action.getActionDescription());
        assertEquals("Miami Beach", action.getLocation());
        assertEquals(5, action.getMinParticipants());
        assertEquals(10, action.getTotalParticipants());
        assertEquals(0, action.getCollectedParticipants());
        assertEquals(start, action.getStartAt());
        assertEquals(end, action.getEndAt());
        assertEquals(ActionStatus.OPEN, action.getActioStatus());
    }

    @Test
    void testGetters() {
        // 🔹 Setup test values
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        String title = "Beach Cleanup";
        String description = "Cleaning plastic waste";
        String location = "Miami Beach";
        int minParticipants = 5;
        int totalParticipants = 10;

        // 🔹 Create object
        ActivismAction action = new ActivismAction(title, description, start, end, location, minParticipants, totalParticipants, organization);

        // 🔹 Verify getters return expected values
        assertEquals(title, action.getTitle());
        assertEquals(description, action.getActionDescription());
        assertEquals(start, action.getStartAt());
        assertEquals(end, action.getEndAt());
        assertEquals(location, action.getLocation());
        assertEquals(minParticipants, action.getMinParticipants());
        assertEquals(totalParticipants, action.getTotalParticipants());
        assertEquals(0, action.getCollectedParticipants()); // ✅ Should start at 0
    }

    @Test
    void testSetters() {
        // 🔹 Setup initial values
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        ActivismAction action = new ActivismAction("Initial Title", "Initial Description", start, end, "Initial Location", 5, 10, organization);

        // 🔹 Modify fields using setters
        action.setTitle("Updated Title");
        action.setActionDescription("Updated Description");
        action.setStartAt(start.plusDays(1));
        action.setEndAt(end.plusDays(1));
        action.setLocation("Updated Location");
        action.setMinParticipants(8);
        action.setTotalParticipants(15);
        action.setCollectedParticipants(3);

        // 🔹 Verify fields are updated correctly
        assertEquals("Updated Title", action.getTitle());
        assertEquals("Updated Description", action.getActionDescription());
        assertEquals(start.plusDays(1), action.getStartAt());
        assertEquals(end.plusDays(1), action.getEndAt());
        assertEquals("Updated Location", action.getLocation());
        assertEquals(8, action.getMinParticipants());
        assertEquals(15, action.getTotalParticipants());
        assertEquals(3, action.getCollectedParticipants());
    }

    @Test
    void shouldReturnTrueForEqualObjects() {
        ActivismAction action1 = new ActivismAction(
                "Beach Cleanup",
                "Cleaning plastic waste",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Miami Beach",
                5,
                10,
                organization
        );

        ActivismAction action2 = new ActivismAction(
                "Beach Cleanup",
                "Cleaning plastic waste",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Miami Beach",
                5,
                10,
                organization
        );

        // Simulate database behavior: set same ID
        action1.setId(1);
        action2.setId(1);

        assertEquals(action1, action2);
        assertEquals(action1.hashCode(), action2.hashCode());
    }


    static class ActionTest {

    }
}







