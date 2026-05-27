package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    ContributeAction action;
    ContributeAction action2;
    Volunteer volunteer;
    Address address;
    LocalDateTime now = LocalDateTime.now();
    Product product;
    ContributeParticipation participation1;
    ContributeParticipation participation2;
    Organization organization;

    @BeforeEach
    public void setup() {
        product = new Product("CLothing", 100, 0);
        address = new Address("Iras 1", 22, "Athens", "11145");
        Address address3 = new Address("pathsiwn", 26, "09876", "athens");
        organization = new Organization("testetstCompany", "company1Password!", "company1Test@outlook.com", "6945679978", address3, "companyTest", "435346787", "Our company related with sustainable clothes", "Company's action is related to design and manufacture friendly environmental clothes", 2010);

        action = new ContributeAction("Clean the Park", "Environmental activity", now.plusDays(1), now.plusDays(3), "Central Parl",  organization);
        action2 = new ContributeAction("Clean the Park2", "Environmental activity2", now.plusDays(1), now.plusDays(3), "Central Parl2",  organization);

        action.addProduct(product);
        volunteer = new Volunteer("elenol_88", "Password123!", "test1@gmail.com", address, "6985212456", "tTest", "Test Tests");
        participation1 = new ContributeParticipation(ParticipationStatus.ACCEPTED, volunteer, action);
        participation2 = new ContributeParticipation(ParticipationStatus.ACCEPTED, volunteer, action2);


        participation1.addParticipationProducts(product, 50);
    }

    @Test
    public void allowProductConstructor() {
        assertEquals(product.getCollectedItems(), 0);
        assertEquals(product.getName(), "CLothing");
        assertEquals(product.getAction(), action);
        assertEquals(product.getRequiredItems(), 100);

    }

    @Test
    public void denySubmitionProduct() throws Exception {
        assertThrows(DomainException.class, () -> {
            Product product = new Product(
                    null, 100, 0
            );

        });
        assertThrows(DomainException.class, () -> {
            Product product = new Product(
                    "Test", -1, 0
            );

        });


        assertThrows(DomainException.class, () -> {
            Product product = new Product(
                    "Test", 0, 0
            );
        });
        assertThrows(DomainException.class, () -> {
            Product product = new Product(
                    "Test", 100, -1
            );
        });
    }

    @Test
    public void testGetters() {
        assertEquals("CLothing", product.getName());
        assertEquals(100, product.getRequiredItems());
        assertEquals(0, product.getCollectedItems());
        assertEquals(action, product.getAction());
        assertTrue(product.getParticipations().contains(participation1));

    }

    @Test
    public void testSetters() {
        product.setName("Clothing Test");
        product.setRequiredItems(1000);
        product.setCollectedItems(300);

        assertEquals("Clothing Test", product.getName());
        assertEquals(1000, product.getRequiredItems());
        assertEquals(300, product.getCollectedItems());
    }

    @Test
    public void allowAddParticipations() {
        product.addParticipation(participation2);

        assertTrue(product.getParticipations().contains(participation2));
        assertTrue(participation2.getProducts().contains(product));
    }

    @Test
    public void denyAddParticipations() throws Exception {
        assertThrows(DomainException.class, () -> {
            product.addParticipation(null);

        });


    }

    @Test
    public void allowRemoveParticipations() {
        product.removeParticipation(participation1);

        assertTrue(!product.getParticipations().contains(participation1));
        assertTrue(!participation1.getProducts().contains(product));
    }

    @Test
    public void denyRemoveParticipations() throws Exception {
        assertThrows(DomainException.class, () -> {
            product.removeParticipation(null);

        });

        assertThrows(DomainException.class, () -> {
            product.removeParticipation(participation2);

        });
    }
}
