package persistence;

import domain.*;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ActionJPATest extends JPATest {


    /**
     * 🔹 Ελέγχει αν όλες οι δράσεις αποθηκεύτηκαν σωστά στη βάση δεδομένων.
     * 🔹 Το `Initializer` πρέπει να έχει προσθέσει **6 συνολικά δράσεις**.
     */
    @Test
    public void listActions() {

        List<Action> result = em.createQuery("SELECT a FROM Action a").getResultList();
        assertEquals(3, result.size());
        System.out.println("size =" + result.size());
        Action action = result.get(0);
        assertNotNull(action.getTitle());
        assertNotNull(action.getActionDescription());
    }

    /**
     * 🔹 Ελέγχει αν οι `ActivismAction` αποθηκεύτηκαν σωστά.
     * 🔹 Περιμένουμε **2 εγγραφές** από τον `Initializer`.
     */
    @Test
    public void listActivismActions() {
        Query query = em.createQuery("SELECT a FROM ActivismAction a");
        List<ActivismAction> result = query.getResultList();
        assertEquals(1, result.size()); // Έχουμε 2 Activism Actions

        ActivismAction activism = result.get(0);
        assertNotNull(activism.getLocation());
        assertTrue(activism.getTotalParticipants() > 0);
    }

    /**
     * 🔹 Ελέγχει αν οι `ContributeAction` αποθηκεύτηκαν σωστά.
     * 🔹 Περιμένουμε **2 εγγραφές** από τον `Initializer`.
     */
    @Test
    public void listContributeActions() {
        Query query = em.createQuery("SELECT c FROM ContributeAction c");
        List<ContributeAction> result = query.getResultList();
        assertEquals(1, result.size()); // Έχουμε 2 Contribute Actions

        ContributeAction contribute = result.get(0);
        assertNotNull(contribute.getLocation());

    }

    /**
     * 🔹 Ελέγχει αν οι `FundingAction` αποθηκεύτηκαν σωστά.
     * 🔹 Περιμένουμε **2 εγγραφές** από τον `Initializer`.
     */
    @Test
    public void listFundingActions() {
        Query query = em.createQuery("SELECT f FROM FundingAction f");
        List<FundingAction> result = query.getResultList();
        assertEquals(1, result.size()); // Έχουμε 2 Funding Actions

        FundingAction funding = result.get(0);
        assertNotNull(funding.getTargetAmount());
        assertTrue(funding.getTargetAmount() > 0);
    }



}
