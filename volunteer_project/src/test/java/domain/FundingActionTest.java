package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.DomainException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FundingActionTest {

    private FundingAction action;
    Organization organization;

    @BeforeEach
    public void setup() {
        Address address3 = new Address("pathsiwn", 26, "09876", "athens");
        organization = new Organization("testetstCompany", "company1Password!", "company1Test@outlook.com", "6945679978", address3, "companyTest" , "435346787", "Our company related with sustainable clothes", "Company's action is related to design and manufacture friendly environmental clothes", 2010);

        action = new FundingAction("School Funding", "Help fund new classrooms",  LocalDateTime.of(2023, 6, 3, 12, 0) ,LocalDateTime.of(2023, 6, 3, 16, 0) ,5000, organization);
    }

    @Test
    void completeActionWhenTargetAmountIsReached() {
        action.donate(3000);
        action.donate(2000); // ✅ Φτάνει το 5000

        assertEquals(5000, action.getCollectedAmount(), 0.01);
        assertEquals(ActionStatus.COMPLETED, action.getActioStatus());
        assertNotNull(action);
    }

    @Test
    void notCompleteActionBeforeReachingTargetAmount() {
        action.donate(3000);

        assertEquals(3000, action.getCollectedAmount(), 0.01);
        assertEquals(ActionStatus.OPEN, action.getActioStatus());
        assertFalse(action.getActioStatus() == ActionStatus.COMPLETED);
    }

    @Test
    void cancelActionWhenDeadlinePassesWithoutEnoughFunds() {
        action.setEndAt(java.time.LocalDateTime.now().minusDays(1));
        action.checkAndDistributeFunds();

        assertEquals(ActionStatus.CANCELLED, action.getActioStatus());
        assertNotSame(ActionStatus.OPEN, action.getActioStatus());
    }

    @Test
    void testGetters() {
        // 🔹 Setup test values
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(10);
        String title = "School Renovation";
        String description = "Raising funds for school renovation";
        float targetAmount = 5000;

        // 🔹 Create object
        FundingAction action = new FundingAction(title, description, start, end, targetAmount, organization);

        // 🔹 Verify getters return expected values
        assertEquals(title, action.getTitle());
        assertEquals(description, action.getActionDescription());
        assertEquals(start, action.getStartAt());
        assertEquals(end, action.getEndAt());
        assertEquals(targetAmount, action.getTargetAmount());
        assertEquals(0, action.getCollectedAmount()); // ✅ Should start at 0
    }

    @Test
    void testSetters() {
        // 🔹 Setup initial values
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(10);
        FundingAction action = new FundingAction("Initial Title", "Initial Description", start, end, 5000, organization);

        // 🔹 Modify fields using setters
        action.setTitle("Updated Title");
        action.setActionDescription("Updated Description");
        action.setStartAt(start.plusDays(1));
        action.setEndAt(end.plusDays(1));
        action.setTargetAmount(10000);
        action.setCollectedAmount(2000);

        // 🔹 Verify fields are updated correctly
        assertEquals("Updated Title", action.getTitle());
        assertEquals("Updated Description", action.getActionDescription());
        assertEquals(start.plusDays(1), action.getStartAt());
        assertEquals(end.plusDays(1), action.getEndAt());
        assertEquals(10000, action.getTargetAmount());
        assertEquals(2000, action.getCollectedAmount());
    }

    @Test
    void testConstructorValidValues() {
        // 🔹 Setup test values
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(30);
        String title = "School Renovation";
        String description = "Raising funds for school renovation";
        float targetAmount = 5000;

        // 🔹 Create object
        FundingAction action = new FundingAction(title, description, start, end, targetAmount, organization);

        // 🔹 Verify all fields are correctly assigned
        assertEquals(title, action.getTitle());
        assertEquals(description, action.getActionDescription());
        assertEquals(start, action.getStartAt());
        assertEquals(end, action.getEndAt());
        assertEquals(targetAmount, action.getTargetAmount());
        assertEquals(0, action.getCollectedAmount()); // ✅ Should start at 0
        assertEquals(ActionStatus.OPEN, action.getActioStatus()); // ✅ Default status should be OPEN
    }
    @Test
    void shouldThrowExceptionWhenDonatingNegativeAmount() {
        Exception exception = assertThrows(DomainException.class, () -> action.donate(-100));
        assertEquals("Deposit amount cannot be negative", exception.getMessage());
    }

    @Test
    void shouldReturnTrueForEqualObjects() {
        FundingAction action1 = new FundingAction(
                "School Renovation",
                "Raising funds for school renovation",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                5000,
                organization
        );

        FundingAction action2 = new FundingAction(
                "School Renovation",
                "Raising funds for school renovation",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                5000,organization
        );

        // Simulate database behavior: set same ID
        action1.setId(1);
        action2.setId(1);

        assertEquals(action1, action2);
        assertEquals(action1.hashCode(), action2.hashCode());
    }



}
