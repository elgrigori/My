package persistence;

import domain.Notification;
import domain.Volunteer;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationJPATest extends JPATest {

    @Test
    public void testNotificationIdAndHashCode() {

        List<Volunteer> volunteers = em.createQuery("SELECT v FROM Volunteer v").getResultList();
        Notification notification1 = new Notification("I am a notification", volunteers.getFirst());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(notification1);
        tx.commit();

        List<Notification> notifications = em.createQuery("SELECT n FROM Notification n").getResultList();

        assertNotNull(notifications.getFirst().getId());
        assertNotNull(notification1.getId());
        assertEquals(notifications.getFirst(), notification1);
        assertSame(notification1, notifications.getFirst());

    }
}
