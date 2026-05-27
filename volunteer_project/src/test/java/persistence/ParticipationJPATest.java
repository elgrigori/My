package persistence;

import static org.junit.jupiter.api.Assertions.*;
import domain.*;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ParticipationJPATest extends JPATest {


    private Validator validator;

    @BeforeEach
    public void createData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void listOfParticipations() {
        List<Participation> participations = em.createQuery("select p from Participation p", Participation.class).getResultList();
        assertEquals(3, participations.size());
        System.out.println("Participations List:");
        for (Participation participation : participations) {
            System.out.println("Participation ID: " + participation.getId());
            assertNotNull(participation.getVolunteer());
            assertNotNull(participation.getAction());
            assertNotNull(participation.getParticipationStatus());
            assertNotNull(participation.getCreatedAt());
        }
    }

    @Test
    public void testListActivismActions() {
        Query query = em.createQuery("SELECT a FROM ActivismAction a");
        List<ActivismAction> result = query.getResultList();
        assertEquals(1, result.size(), "Πρέπει να υπάρχει μία καταχωρημένη δράση ακτιβισμού");

        ActivismAction activism = result.get(0);
        assertNotNull(activism.getLocation(), "Η τοποθεσία δεν πρέπει να είναι null");
        assertTrue(activism.getTotalParticipants() > 0, "Οι συνολικοί συμμετέχοντες πρέπει να είναι μεγαλύτεροι από 0");
    }











}
