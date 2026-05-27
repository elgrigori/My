package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SystemDateTime;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
    private Action action;
    private Organization organization;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        SystemDateTime.setStub(now);

        organization = new Organization("orgUser", "password123!", "org@mail.com",
                "1234567890", new Address("Street", 12, "11111", "City"),
                "Organization Name", "123456789", "Description", "Action Description", 2000);

        action = new Action("Cleanup Drive", "Cleaning public places", now.plusDays(1), now.plusDays(3), organization);
    }

    @Test
    public void testActionConstructor() {
        assertEquals("Cleanup Drive", action.getTitle());
        assertEquals("Cleaning public places", action.getActionDescription());
        assertEquals(now.plusDays(1), action.getStartAt());
        assertEquals(now.plusDays(3), action.getEndAt());
        assertEquals(ActionStatus.OPEN, action.getActioStatus());
        assertEquals(organization, action.getOrganization());
    }

    @Test
    public void testCancelAction() {
        assertDoesNotThrow(() -> action.cancelAction());
        assertEquals(ActionStatus.CANCELLED, action.getActioStatus());
    }

    @Test
    public void testCancelActionTooLate() {
        action.setStartAt(now.minusHours(5));
        assertThrows(DomainException.class, () -> action.cancelAction());
    }

    @Test
    public void testCompleteActionBeforeDeadline() {
        assertThrows(DomainException.class, () -> action.completeAction());
    }

    @Test
    public void testCompleteActionAfterDeadline() {
        action.setEndAt(now.minusHours(1));
        assertDoesNotThrow(() -> action.completeAction());
        assertEquals(ActionStatus.COMPLETED, action.getActioStatus());
    }



    @Test
    public void testOngoingAndExpired() {
        assertFalse(action.isOngoing());
        assertFalse(action.isExpired());

        action.setStartAt(now.minusDays(1));
        action.setEndAt(now.plusDays(1));
        assertTrue(action.isOngoing());
        assertFalse(action.isExpired());

        action.setEndAt(now.minusDays(1));
        assertFalse(action.isOngoing());
        assertTrue(action.isExpired());
    }
}