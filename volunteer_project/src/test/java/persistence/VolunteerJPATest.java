package persistence;

import static org.junit.jupiter.api.Assertions.*;
import domain.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VolunteerJPATest extends  JPATest {

    @Test
    public void testParticipateInAction() {
        List<Volunteer> volunteers = em.createQuery("select v from Volunteer v").getResultList();
        Volunteer volunteerUser = volunteers.getFirst();

        List<Action> actions = em.createQuery("select a from Action a").getResultList();
        int previous_participations = volunteerUser.getParticipations().size();

        volunteerUser.participateInAction(actions.get(0));
        volunteerUser.participateInAction(actions.get(1));
        volunteerUser.participateInAction(actions.get(2));
        Action actionTest = new Action();

        assertThrows(DomainException.class, () -> {
            volunteerUser.participateInAction(actionTest);
        });

        assertEquals(previous_participations + 3, volunteerUser.getParticipations().size());

    }


        @Test
    public void testInvalidParticipateInAction() {
        List<Volunteer> volunteers = em.createQuery("select v from Volunteer v").getResultList();
        Volunteer volunteerUser = volunteers.getFirst();

        List<Action> actions = em.createQuery("select a from Action a").getResultList();
        Action action1 = actions.get(0);
        Action action2 = actions.get(1);

        assertThrows(DomainException.class, () -> {
            volunteerUser.participateInAction(null);
        });
        assertThrows(DomainException.class, () -> {
            action1.setActionStatus(ActionStatus.COMPLETED);
            volunteerUser.participateInAction(action1);
        });
        assertThrows(DomainException.class, () -> {
            action2.setActionStatus(ActionStatus.CANCELLED);
            volunteerUser.participateInAction(action2);
        });

        assertThrows(DomainException.class, () -> {
            volunteerUser.participateInAction(action2);
        });

    }

        @Test
    public void cancelParticipateInAction() {
        List<Volunteer> volunteers = em.createQuery("select v from Volunteer v").getResultList();
        Volunteer volunteerUser = volunteers.get(0);

        List<Action> actions = em.createQuery("select a from Action a").getResultList();
        Action action = actions.getFirst();
        volunteerUser.participateInAction(action);

        List<Participation> userParticipations = new ArrayList<Participation>(volunteerUser.getParticipations());

            userParticipations.get(0).getAction().setStartAt(LocalDateTime.now().plusDays(1));
            volunteerUser.cancelParticipation(userParticipations.get(0));
            assertEquals(ParticipationStatus.CANCELLED, userParticipations.get(0).getParticipationStatus());

            userParticipations.get(0).getAction().setStartAt(LocalDateTime.now().minusDays(1));
            assertThrows(Exception.class, () -> {
                volunteerUser.cancelParticipation(userParticipations.get(0));
            });

            userParticipations.get(0).getAction().setActionStatus(ActionStatus.COMPLETED);
            assertThrows(Exception.class, () -> {
                volunteerUser.cancelParticipation(userParticipations.get(0));
            });

            userParticipations.get(0).getAction().setActionStatus(ActionStatus.CANCELLED);
            assertThrows(Exception.class, () -> {
                volunteerUser.cancelParticipation(userParticipations.get(0));
            });

            assertThrows(Exception.class, () -> {
                volunteerUser.cancelParticipation(userParticipations.get(0));
            });

    }

}
