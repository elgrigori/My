package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ContributeActionTest {

    private ContributeAction action;
    private Organization organization;
    private Product product1, product2;

    @BeforeEach
    void setup() {
        // Δημιουργία οργανισμού με όλα τα απαραίτητα στοιχεία
        Address address = new Address("Main Street", 10, "12345", "Athens");
        organization = new Organization("org_username", "SecurePass123!", "org@example.com", "6999999999",
                address, "Green Organization", "123456789", "Non-Profit", "Helping the environment", 2010);

        // Δημιουργία δράσης
        action = new ContributeAction(
                "Food Donation", "Collect food for the needy",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5),
                "Community Center", organization
        );

        // Δημιουργία προϊόντων
        product1 = new Product("Rice", 50, 0);
        product2 = new Product("Beans", 30, 0);
    }

    @Test
    void testConstructorValidValues() {
        assertEquals("Food Donation", action.getTitle());
        assertEquals("Collect food for the needy", action.getActionDescription());
        assertEquals("Community Center", action.getLocation());
        assertEquals(organization, action.getOrganization());
        assertEquals(ActionStatus.OPEN, action.getActioStatus());
    }

    @Test
    void testConstructorInvalidValues() {
        assertThrows(DomainException.class, () ->
                new ContributeAction(null, "Valid Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Valid Location", organization));

        assertThrows(DomainException.class, () ->
                new ContributeAction("Valid Title", "Valid Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, organization));

        assertThrows(DomainException.class, () ->
                new ContributeAction("Valid Title", "Valid Description", LocalDateTime.now(), LocalDateTime.now().minusDays(1), "Valid Location", organization));

        assertThrows(DomainException.class, () ->
                new ContributeAction("Valid Title", "Valid Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Valid Location", null));
    }

    @Test
    void shouldAddProductSuccessfully() {
        action.addProduct(product1);
        action.addProduct(product2);

        assertEquals(2, action.getProducts().size());
        assertTrue(action.getProducts().contains(product1));
        assertTrue(action.getProducts().contains(product2));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullProduct() {
        Exception exception = assertThrows(DomainException.class, () -> action.addProduct(null));
        assertEquals("Το προϊόν δεν μπορεί να είναι κενό.", exception.getMessage());
    }

    @Test
    void shouldRemoveProductSuccessfully() {
        action.addProduct(product1);
        action.addProduct(product2);

        action.removeProduct(product1);

        assertEquals(1, action.getProducts().size());
        assertFalse(action.getProducts().contains(product1));
    }



    @Test
    void shouldCompleteActionWhenDeadlinePasses() {
        action.setEndAt(LocalDateTime.now().minusDays(1)); // Έληξε
        action.completeAction();

        assertEquals(ActionStatus.COMPLETED, action.getActioStatus());
    }




    @Test
    void shouldCancelActionWhenDeadlinePassesWithoutEnoughProducts() {
        action.setEndAt(LocalDateTime.now().minusDays(1)); // Έληξε χωρίς προϊόντα
        action.checkAndCancelAction();

        assertEquals(ActionStatus.CANCELLED, action.getActioStatus());
    }


}
