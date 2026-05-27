package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SystemDateTime;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ContributeParticipationTest {
    Volunteer volunteer;
    Address address;
    ContributeAction action;
    LocalDateTime now = LocalDateTime.now();
    ContributeParticipation participation;
    Product product;

    @BeforeEach
    void setUp() {
        SystemDateTime.setStub(now);
        address = new Address();
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

        product = new Product("Clothes", 100, 0);
        action = new ContributeAction();
        action.setTitle("New Action");
        action.setActionDescription("This is an action");
        action.setLocation("Central Park");
        action.addProduct(product);


        participation = new ContributeParticipation();
        participation.setAction(new Action());
        participation.setVolunteer(volunteer);
        participation.setCreatedAt(now);
        participation.addParticipationProducts(product, 50);

    }

    @Test
    public void testContributeParticipationConstructor() {

        // Create an instance of ContributeParticipation
        ContributeParticipation contributeParticipation = new ContributeParticipation(
                ParticipationStatus.ACCEPTED, volunteer, action
        );

        // Validate fields
        assertEquals(contributeParticipation.getParticipationStatus(), ParticipationStatus.ACCEPTED);
        assertEquals(contributeParticipation.getVolunteer(), volunteer);
        assertEquals(contributeParticipation.getCreatedAt(), now);

    }

    @Test
    public void denyCOntributearticipationConstructor() {
        assertThrows(DomainException.class, () -> {
            ContributeParticipation contributeParticipation = new ContributeParticipation(
                    ParticipationStatus.ACCEPTED, volunteer, null
            );

        });
        assertThrows(DomainException.class, () -> {
            ContributeParticipation contributeParticipation = new ContributeParticipation(
                    ParticipationStatus.ACCEPTED, null, action
            );

        });


        assertThrows(DomainException.class, () -> {
            ContributeParticipation contributeParticipation = new ContributeParticipation(
                    ParticipationStatus.ACCEPTED, null, null
            );

        });


    }

    @Test
    public void allowSetProducts() {
        Set<Product> products = new HashSet<Product>();
        products.add(product);
        products.add(new Product("Food", 100, 0));

        assertEquals(2, products.size());
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullProducts() throws DomainException {
        assertThrows(DomainException.class, () -> {
            participation.setProducts(null);

        });
    }

    @Test
    public void allowAddParticipationsProducts() {


        participation.addParticipationProducts(product, 5);
        assertEquals(1, participation.getParticipationProducts().size());

    }


}

