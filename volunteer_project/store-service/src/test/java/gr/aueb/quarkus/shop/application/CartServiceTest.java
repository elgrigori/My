package gr.aueb.quarkus.shop.application;

import gr.aueb.quarkus.shop.common.JPATest;
import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderRepository;
import gr.aueb.quarkus.shop.infrastructure.service.warehouse.WarehouseApi;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static gr.aueb.quarkus.shop.fixture.EntityFixture.ValueFixture.creditCard;
import static org.junit.jupiter.api.Assertions.*;

/**
 * No Stub implementation is needed and no configuration in the application.properties file.
 * The test substitutes managed beans with the help of Mockito and appropriate Quarkus support.
 */
@QuarkusTest
class CartServiceTest extends JPATest {

    @InjectMock
    PaymentService paymentService;

    @RestClient
    @InjectMock
    WarehouseApi warehouseApi;

    @Inject
    CartService cartService;

    @Inject
    OrderRepository orderRepository;

    @BeforeEach
    public void setup(){
        Mockito.when(paymentService.pay(Mockito.any(CreditCard.class), Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(warehouseApi.reserveProductStock(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Response.noContent().build());
    }

    @Test
    void getCurrentCartThatExists() {
        Cart cart = cartService.getCurrentCart(101l);
        assertEquals(1, cart.getCartItems().size());
        assertEquals(101, cart.getCustomer().getId());
    }

    @Test
    void getCurrentCartThatNotExists() {
        Cart cart = cartService.getCurrentCart(100l);
        assertEquals(0, cart.getCartItems().size());
        assertEquals(100, cart.getCustomer().getId());
    }

    @Test
    void checkoutExistingCart(){

        Long orderId = cartService.checkout(500l, creditCard());
        assertNotNull(orderId);

        Optional<Order> orderOptional = orderRepository.orderWithOrderLines(orderId);
        assertTrue(orderOptional.isPresent());
        assertEquals(1, orderOptional.get().getOrderLines().size());

    }
}