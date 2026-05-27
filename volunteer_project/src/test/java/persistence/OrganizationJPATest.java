package persistence;

import domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationJPATest extends JPATest {

    @Test
    public void testDeleteAction() {

        List<Organization> organizations = em.createQuery("SELECT o FROM Organization o").getResultList();
        System.out.println("Organization List:");
        for (Organization organization : organizations) {
            System.out.println(organization.getActions());
        }
        Organization org = organizations.get(0);

        List<Action> actions = new ArrayList<Action>(org.getActions());

        org.createAction(new Action());
        assertThrows(Exception.class, () -> {
            org.createAction(actions.get(0));
        });
        assertThrows(Exception.class, () -> {
            org.createAction(null);
        });
        actions.get(0).setStartAt(LocalDateTime.now().plusDays(1));
        org.cancelAction(actions.get(0));
        assertEquals(ActionStatus.CANCELLED, actions.get(0).getActioStatus());

        actions.get(1).setStartAt(LocalDateTime.now().minusDays(1));
        assertThrows(Exception.class, () -> {
            org.cancelAction(actions.get(1));
        });

        assertThrows(Exception.class, () -> {
            org.cancelAction(new Action());
        });
    }
}
