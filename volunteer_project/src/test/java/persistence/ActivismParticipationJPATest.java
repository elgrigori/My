package persistence;

import domain.*;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivismParticipationJPATest extends JPATest {
    private Address testAddress;
    private Validator validator;

    @BeforeEach
    public void createData() {

        testAddress = new Address("testStreet", 55, "12345", "thessaloniki");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void listOfActivismParticipations() {
        List<ActivismParticipation> participations = em.createQuery(
                "SELECT p FROM Participation p WHERE TYPE(p) = ActivismParticipation",
                ActivismParticipation.class
        ).getResultList();

        assertEquals(1, participations.size());

        ActivismParticipation participation1 = participations.get(0);

        assertNotNull(participation1.getId());
        assertNotNull(participation1.getVolunteer());
        assertNotNull(participation1.getAction())  ;
        assertNotNull(participation1.getParticipationStatus());

    }
    @Test
    @Transactional
    public void listActivismParticipationsByCorrespondentVolunteer()
    {
       Query query= em.createQuery("Select p FROM Participation p  WHERE TYPE(p) = ActivismParticipation and  p.volunteer.email = :email");
        query.setParameter("email", "volunteer1@gmail.com");
        List<Participation> result = query.getResultList();
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void allowAddNewActivismParticitipationForCorrenspandingVolunteer()
    {


        Query query= em.createQuery("Select v FROM Volunteer v  WHERE email = :email");
        query.setParameter("email", "volunteer1@gmail.com");
        Volunteer volunteer = (Volunteer) query.getSingleResult();

        Query action= em.createQuery("Select a FROM Action a  WHERE title = :title");
        action.setParameter("title", "Beach Cleanup");
        ActivismAction activismAction = (ActivismAction) action.getSingleResult();
        ActivismParticipation activismParticipation = new ActivismParticipation(
                ParticipationStatus.ACCEPTED, volunteer, activismAction
        );
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 🔹 Πρώτα αποθηκεύουμε ΟΡΓΑΝΙΣΜΟΥΣ και ΕΘΕΛΟΝΤΕΣ
        em.persist(activismParticipation);
        em.persist(volunteer);
        em.flush();
        tx.commit();

        Query paricipations= em.createQuery("Select p FROM Participation p  WHERE TYPE(p) = ActivismParticipation and  p.volunteer.email = :email");
        query.setParameter("email", "volunteer1@gmail.com");
        List<Participation> result = query.getResultList();
        assertEquals(1, result.size());
        em.close();
    }

    @Test
    @Transactional
    public void allowRemoveNewActivismParticitipationForCorrenspandingVolunteer()
    {


        Query query= em.createQuery("Select v FROM Volunteer v  WHERE email = :email");
        query.setParameter("email", "volunteer1@gmail.com");
        Volunteer volunteer = (Volunteer) query.getSingleResult();

        Query action= em.createQuery("Select a FROM Action a  WHERE title = :title");
        action.setParameter("title", "Beach Cleanup");
        ActivismAction activismAction = (ActivismAction) action.getSingleResult();
        ActivismParticipation activismParticipation = new ActivismParticipation(
                ParticipationStatus.ACCEPTED, volunteer, activismAction
        );
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 🔹 Πρώτα αποθηκεύουμε ΟΡΓΑΝΙΣΜΟΥΣ και ΕΘΕΛΟΝΤΕΣ
        em.persist(activismParticipation);
        em.persist(volunteer);
        em.flush();
        tx.commit();

        Query paricipations= em.createQuery("Select p FROM Participation p  WHERE TYPE(p) = ActivismParticipation and  p.volunteer.email = :email");
        query.setParameter("email", "volunteer1@gmail.com");
        List<Participation> result = query.getResultList();
        assertEquals(1, result.size());
        em.close();
    }

}
