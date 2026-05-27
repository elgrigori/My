package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.cart.CartItem;
import gr.aueb.quarkus.shop.fixture.EntityFixture;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CartMapperTest {

    @Inject
    CartMapper cartMapper;

    @Test
    void toRepresentation() {

        Cart cart = EntityFixture.CartFixture.cartWithItem();
        CartRepresentation representation = cartMapper.toRepresentation(cart);

        assertEquals(cart.getId(), representation.id);
        assertEquals(cart.getCustomer().getId(), representation.customer.id);

        CartItem cartItem = cart.getCartItems().stream().findFirst().get();
        CartItemRepresentation itemRepresentation = representation.cartItems.get(0);

        assertEquals(cartItem.getProduct().getId(), itemRepresentation.product.id);
        assertEquals(cartItem.getQuantity(), itemRepresentation.quantity);

    }
}