//package persistence;
//
//
//
//import domain.*;
//
//import jakarta.transaction.Transactional;
//import org.hibernate.grammars.hql.HqlParser;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ContributeParticipationJPATest extends JPATest {
//
//    Organization organization = new Organization();
//
//    @Test
//    @Transactional
//    public void testCreateAndPersistContributeAction() {
//        List<ContributeParticipation> participations = em.createQuery(
//                "SELECT p FROM Participation p WHERE TYPE(p) = ContributeParticipation x",
//                ContributeParticipation.class
//        ).getResultList();
//
//        assertEquals(1, participations.size());
//
//        ContributeParticipation participation1 = participations.get(0);
//
//        assertNotNull(participation1.getId());
//        assertNotNull(participation1.getVolunteer());
//        assertNotNull(participation1.getAction())  ;
//    }
//    @Test
//    @Transactional
//    public void testAddProductToContributeAction() throws Exception {
//        em.getTransaction().begin();
//
//        ContributeAction contributeAction = new ContributeAction(
//                "Clothing Donation",
//                "Collect winter clothes",
//                LocalDateTime.now().plusDays(1),
//                LocalDateTime.now().plusDays(5),
//                "Donation Center",
//                50,
//                organization
//        );
//        em.persist(contributeAction);
//        em.flush();
//
//        Product product = new Product();
//        product.setName("Jacket");
//        product.setAction(contributeAction);
//        em.persist(product);
//        em.flush();
////        contributeAction.addProduct(product);
//
//
//        em.getTransaction().commit();
//
//        ContributeAction found = em.find(ContributeAction.class, contributeAction.getId());
//        assertNotNull(found);
//        //assertEquals(1, found.γρ().size());
//    }
//   // @Test
//    //@Transactional
////    public void testCheckAndCompleteContributeAction() {
////
////        em.getTransaction().begin();
////
////        ContributeAction contributeAction = new ContributeAction(
////                "Toy Donation",
////                "Collect toys for children",
////                LocalDateTime.now().plusDays(1),
////                LocalDateTime.now().plusDays(5),
////                "Orphanage",
////                20
////        );
////        em.persist(contributeAction);
////        em.flush();
////
////        em.getTransaction().commit();
////
////        em.getTransaction().begin();
////        contributeAction.addItems(20);
////        em.merge(contributeAction);
////        em.getTransaction().commit();
////
////        ContributeAction found = em.find(ContributeAction.class, contributeAction.getId());
////        assertEquals(20, found.getCollectedItems());
////    }
//    @Test
//    @Transactional
//
//    public void listOfParticipations() {
//
//
//        List<Participation> result = em.createQuery("SELECT p FROM Participation p").getResultList();
//        assertEquals(6, result.size());
//
//
//        Participation participation = result.get(0);
//        assertNotNull(participation.getVolunteer());
//        assertNotNull(participation.getAction());
//    }
//
//    @Test
//    @Transactional
//    public void testCancelParticipation() {
//        em.getTransaction().begin();
//
//        Address address = new Address() ;
//        address.setStreet("Test123");
//        address.setStreetNumber(12);
//        address.setCity("Athens");
//        address.setPostalCode("11475");
//
//       // Create a volunteer
//        Volunteer volunteer = new Volunteer();
//        volunteer.setUsername("volunteer51");
//        volunteer.setPassword("Password1231!");
//        volunteer.setFirstName("John51");
//        volunteer.setLastName("Doe15");
//        volunteer.setEmail("joh5n1@doe.com");
//        volunteer.setMobile("685212458");
//        volunteer.setAddress(address);
//        em.persist(volunteer);
//
//
//        ContributeAction action = new ContributeAction(
//                "Food Drive",
//                "Collect non-perishable food",
//                LocalDateTime.now().plusDays(3),
//                LocalDateTime.now().plusDays(7),
//                "Community Center",
//                50,
//                organization
//        );
//        em.persist(action);
//        em.flush();
//        Participation participation = new Participation(ParticipationStatus.ACCEPTED, volunteer, action);
//        em.persist(participation);
//        em.getTransaction().commit();
//
//        em.getTransaction().begin();
//        em.merge(participation);
//        em.getTransaction().commit();
//
//        Participation found = em.find(Participation.class, participation.getId());
//        assertEquals(ParticipationStatus.CANCELLED, found.getParticipationStatus());
//    }
//
//}
//
