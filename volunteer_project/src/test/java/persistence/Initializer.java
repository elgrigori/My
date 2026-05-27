package persistence;

import domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;


public class Initializer {

    private final EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }


//    public void eraseData(EntityManager em) {
//        EntityTransaction tx = em.getTransaction();
//        if (!tx.isActive()) {  // Prevent starting a new transaction if one is active
//            tx.begin();
//        }
//
//        em.createQuery("DELETE FROM Participation").executeUpdate();
//        em.createQuery("DELETE FROM Volunteer").executeUpdate();
//
//        tx.commit();
//    }

    public void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        em.createNativeQuery("DELETE FROM participations").executeUpdate();


        em.createNativeQuery("DELETE FROM notifications").executeUpdate();

        em.createNativeQuery("DELETE FROM products").executeUpdate();
        em.createNativeQuery("DELETE FROM action").executeUpdate();
        em.createNativeQuery("delete from users").executeUpdate();

//        em.createQuery("DELETE FROM Volunteer").executeUpdate();
        tx.commit();
//        em.close();
    }

    public void prepareData() {
        eraseData();

        // Δημιουργία Εθελοντών
        Address address1 = new Address("ermou", 11, "12345", "athens");
        Volunteer volunteer1 = new Volunteer("volunteer1", "volunteer1Pass!", "volunteer1@gmail.com", address1, "6978675689", "Mary", "Papadopoulou");

        Address address2 = new Address("thessalonikis", 12, "11852", "athens");
        Volunteer volunteer2 = new Volunteer("volunteer2", "volunteer2Password!", "volunteer2@gmail.com", address2, "6971234567", "John", "Papazoglou");

        // Δημιουργία Οργανισμού
        Address orgAddress = new Address("pathsiwn", 26, "09876", "athens");
        Organization organization1 = new Organization("company1username", "company1Password!", "company1@outlook.com", "6945679978", orgAddress, "casualOutfits", "001234567", "Sustainable clothes", "Design and manufacture", 2010);
        Product product1 =  new Product("Food",100,0);
        Product procuct2 = new Product("Shoes",200,0);


        // Δημιουργία Δράσεων (Action)
        ContributeAction contributeAction = new ContributeAction(
                "Food Drive", "Collecting food for homeless",
                LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                "Community Center", organization1
        );

        ActivismAction activismAction = new ActivismAction(
                "Beach Cleanup", "Cleaning plastic waste",
                LocalDateTime.now(), LocalDateTime.now().plusDays(7),
                "Miami Beach", 5, 10, organization1
        );

        FundingAction fundingAction = new FundingAction(
                "School Renovation", "Raising funds for school renovation",
                LocalDateTime.now(), LocalDateTime.now().plusDays(30),
                5000, organization1
        );

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 🔹 Πρώτα αποθηκεύουμε ΟΡΓΑΝΙΣΜΟΥΣ και ΕΘΕΛΟΝΤΕΣ
        em.persist(organization1);
        em.persist(volunteer1);
        em.persist(volunteer2);



        // 🔹 Μετά αποθηκεύουμε τις ΔΡΑΣΕΙΣ
        em.persist(contributeAction);
        em.persist(activismAction);
        em.persist(fundingAction);

        // 🔹 Βεβαιωνόμαστε ότι οι δράσεις έχουν ID πριν δημιουργήσουμε συμμετοχές
        em.flush();
        activismAction.addParticipant();
        contributeAction.addProduct(product1);
        contributeAction.addProduct(procuct2);
        em.persist(product1);
        em.persist(procuct2);
        // 🔹 Δημιουργία και αποθήκευση Συμμετοχών (με σωστή αναφορά σε `action`)
        ContributeParticipation contributeParticipation = new ContributeParticipation(
                ParticipationStatus.ACCEPTED, volunteer1, contributeAction
        );
        contributeParticipation.addParticipationProducts(procuct2,40);
        contributeParticipation.addParticipationProducts(product1,20);

        ActivismParticipation activismParticipation = new ActivismParticipation(
                ParticipationStatus.ACCEPTED, volunteer1, activismAction
        );


        FundingParticipation fundingParticipation = new FundingParticipation(
                ParticipationStatus.ACCEPTED, volunteer2, fundingAction, 100
        );


//        volunteer1.addParticipation(activismParticipation);
//        volunteer1.addParticipation(contributeParticipation);
//        volunteer2.addParticipation(fundingParticipation);
        // 🔹 Αποθήκευση συμμετοχών
        em.persist(contributeParticipation);
        em.persist(activismParticipation);
        em.persist(fundingParticipation);
        em.flush();

        tx.commit();
        em.close();
    }


}
