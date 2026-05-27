//package util;
//
//import domain.Action;
//import domain.ActionStatus;
//import domain.Organization;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import domain.DomainException;
//
//import util.SystemDateTime;
//import java.time.LocalDateTime;
//import static org.junit.jupiter.api.Assertions.*;
//
//class ActionTest {
//
//    private Action action;
//    private Action ongoingAction;
//    private Action expiredAction;
//    private Action futureAction;
//    private Organization organization;
//    @BeforeEach
//    void setup() {
//        organization = new Organization("testetstCompany", "company1Password!", "company1Test@outlook.com", "6945679978", address3, "companyTest" , "435346787", "Our company related with sustainable clothes", "Company's action is related to design and manufacture friendly environmental clothes", 2010);
//
//        // 🔹 Create a valid action before each test
//        action = new Action(
//                "Community Cleanup",
//                "Organizing a local park cleanup",
//                SystemDateTime.now().plusDays(3), // Start date in the future
//                SystemDateTime.now().plusDays(5),  // End date also in the future
//                organization
//        );
//
//        LocalDateTime now = SystemDateTime.now();
//
//        // 🔹 Action that is currently ongoing
//        ongoingAction = new Action(
//                "Ongoing Cleanup",
//                "Cleaning the local park",
//                now.minusDays(1),  // Started yesterday
//                now.plusDays(1)  ,  // Ends tomorrow
//                organization
//        );
//
//        // 🔹 Action that has already expired
//        expiredAction = new Action(
//                "Expired Fundraiser",
//                "Raising money for charity",
//                now.minusDays(5),  // Started 5 days ago
//                now.minusDays(1),   // Expired yesterday
//                organization
//        );
//
//        // 🔹 Action that will start in the future
//        futureAction = new Action(
//                "Future Event",
//                "Upcoming volunteering event",
//                now.plusDays(2),  // Starts in 2 days
//                now.plusDays(5) ,  // Ends in 5 days
//                organization
//        );
//    }
//
//    @Test
//    void testConstructor() {
//        // 🔹 Check if constructor assigns values correctly
//        assertEquals("Community Cleanup", action.getTitle());
//        assertEquals("Organizing a local park cleanup", action.getActionDescription());
//        assertEquals(ActionStatus.OPEN, action.getActioStatus());
//        assertTrue(action.getStartAt().isAfter(SystemDateTime.now()));
//        assertTrue(action.getEndAt().isAfter(action.getStartAt()));
//    }
//
//    @Test
//    void testGetters() {
//        // 🔹 Verify getters return expected values
//        assertEquals("Community Cleanup", action.getTitle());
//        assertEquals("Organizing a local park cleanup", action.getActionDescription());
//        assertEquals(ActionStatus.OPEN, action.getActioStatus());
//    }
//
//    @Test
//    void testSetters() {
//        // 🔹 Modify fields using setters
//        action.setTitle("Updated Title");
//        action.setActionDescription("Updated Description");
//        action.setStartAt(SystemDateTime.now().plusDays(10));
//        action.setEndAt(SystemDateTime.now().plusDays(15));
//        action.setActionStatus(ActionStatus.COMPLETED);
//
//        // 🔹 Verify fields are updated correctly
//        assertEquals("Updated Title", action.getTitle());
//        assertEquals("Updated Description", action.getActionDescription());
//        assertTrue(action.getStartAt().isAfter(SystemDateTime.now()));
//        assertTrue(action.getEndAt().isAfter(action.getStartAt()));
//        assertEquals(ActionStatus.COMPLETED, action.getActioStatus());
//    }
//
//    @Test
//    void testCancelActionBeforeStart() {
//        // 🔹 Cancel action more than 12 hours before start
//        action.setStartAt(SystemDateTime.now().plusDays(2));
//        action.cancelAction();
//
//        // 🔹 Ensure status is updated correctly
//        assertEquals(ActionStatus.CANCELLED, action.getActioStatus());
//    }
//
//    @Test
//    void testCancelActionWithin12HoursBeforeStart() {
//        // 🔹 Set start time within 12 hours from now
//        action.setStartAt(SystemDateTime.now().plusHours(8));
//
//        // 🔹 Attempting to cancel should fail
//        Exception exception = assertThrows(DomainException.class, () -> action.cancelAction());
//        assertEquals("Cancellation is only allowed at least 12 hours before the action starts.", exception.getMessage());
//    }
//
//    @Test
//    void testCancelActionAfterDeadline() {
//        // 🔹 Set action end time in the past
//        action.setEndAt(SystemDateTime.now().minusDays(1));
//
//        // 🔹 Attempting to cancel should fail
//        Exception exception = assertThrows(DomainException.class, () -> action.cancelAction());
//        assertEquals("Cannot cancel an already completed action", exception.getMessage());
//    }
//
//    @Test
//    void testCompleteActionSuccessfully() {
//        // 🔹 Set end time in the past to allow completion
//        action.setEndAt(SystemDateTime.now().minusDays(1));
//        action.completeAction();
//
//        // 🔹 Ensure action status is updated
//        assertEquals(ActionStatus.COMPLETED, action.getActioStatus());
//    }
//
//    @Test
//    void testCompleteActionBeforeDeadline() {
//        // 🔹 Set end time in the future
//        action.setEndAt(SystemDateTime.now().plusDays(1));
//
//        // 🔹 Attempting to complete early should fail
//        Exception exception = assertThrows(DomainException.class, () -> action.completeAction());
//        assertEquals("Action cannot be completed before its deadline.", exception.getMessage());
//    }
//
//    // ✅ Added Tests for `isOngoing()` and `isExpired()`
//    @Test
//    void testIsOngoing() {
//        assertTrue(ongoingAction.isOngoing());
//        assertFalse(expiredAction.isOngoing());
//        assertFalse(futureAction.isOngoing());
//    }
//
//    @Test
//    void testIsExpired() {
//        assertTrue(expiredAction.isExpired());
//        assertFalse(ongoingAction.isExpired());
//        assertFalse(futureAction.isExpired());
//    }
//}
