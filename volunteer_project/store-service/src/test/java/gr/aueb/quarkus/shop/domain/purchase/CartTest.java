package gr.aueb.quarkus.shop.domain.purchase;

import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.cart.CartItem;
import gr.aueb.quarkus.shop.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Product productA;
    private Product productB;

    @BeforeEach
    public void setup(){
        cart = new Cart();
        productA = new Product();
        productA.setId(1l);
        productB = new Product();
        productB.setId(2l);
    }
    @Test
    void addItem() {
        cart.addItem(productA, 1);
        cart.addItem(productB, 2);

        Optional<CartItem> itemResult = cart.getItem(productA);
        assertTrue(itemResult.isPresent());
        assertEquals(1, itemResult.get().getQuantity());

        itemResult = cart.getItem(productB);
        assertTrue(itemResult.isPresent());
        assertEquals(2, itemResult.get().getQuantity());

    }

    @Test
    void addSameProduct(){
        cart.addItem(productA, 1);
        cart.addItem(productA, 2);

        Optional<CartItem> itemResult = cart.getItem(productA);
        assertTrue(itemResult.isPresent());
        assertEquals(3, itemResult.get().getQuantity());
    }
}