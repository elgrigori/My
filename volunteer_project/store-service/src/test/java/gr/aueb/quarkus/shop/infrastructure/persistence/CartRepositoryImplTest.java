package gr.aueb.quarkus.shop.infrastructure.persistence;

import gr.aueb.quarkus.shop.common.JPATest;
import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.cart.CartItem;
import gr.aueb.quarkus.shop.domain.cart.CartRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CartRepositoryImplTest extends JPATest {

    @Inject
    CartRepository cartRepository;

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    void findCartForCustomer() {
        Optional<Cart> cartOptional = cartRepository.findCartWithItemsForCustomer(101l);
        assertTrue(cartOptional.isPresent());

        Cart cart = cartOptional.get();
        entityManager.detach(cart);

        CartItem cartItem = cart.getCartItems().stream().findFirst().get();
        assertEquals(201, cartItem.getProduct().getId());

    }

    @Test
    void cartOfId() {
    }
}