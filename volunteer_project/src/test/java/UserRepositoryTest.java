////public class UserRepositoryTest {
////}
//
////package com.example.repository;
//
//import domain.User;
////import org.testng.annotations.Test;
//
//
////import org.junit.jupiter.api.*;
//
//
//import volunteer.aueb.UserRepository;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class UserRepositoryTest {
//
//    private UserRepository userRepository;
//
//    @BeforeAll
//    void setup() {
//        userRepository = new UserRepository();
//    }
//
//    @Test
//    public void testSaveAndFindById() {
//        User user = new User("John Doe", "john@example.com");
//        userRepository.save(user);
//
//        User foundUser = userRepository.findById(user.getId());
//        assertNotNull(foundUser);
//        assertEquals("John Doe", foundUser.getName());
//    }
//
//    @Test
//    public void testFindAll() {
//        userRepository.save(new User("Alice", "alice@example.com"));
//        userRepository.save(new User("Bob", "bob@example.com"));
//
//        List<User> users = userRepository.findAll();
//        User user1 = userRepository.findById(users.getFirst().getId());
//        assertTrue(users.size() >= 2);
//
////        assertNotNull(savedUser.getId());  // Το ID πρέπει να έχει δημιουργηθεί
//        assertEquals("Alice", user1.getName());
////        assertEquals("Bob", user1.getName()); // this will failure the tests
////        assertEquals("john@example.com", savedUser.getEmail());
//    }
//}
//
