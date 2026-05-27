package gr.aueb.quarkus.shop.application;

import gr.aueb.quarkus.shop.common.JPATest;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static gr.aueb.quarkus.shop.fixture.EntityFixture.ValueFixture.creditCard;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing with stubs implemented by @Alternative annotated beans requires
 * their declaration/activation in application.properties.
 * For the specific test case the following property is required:
 *
 * quarkus.arc.selected-alternatives=gr.aueb.quarkus.shop.infrastructure.service.warehouse.WarehouseApiStub
 */
@QuarkusTest
class CartServiceWithStubTest extends JPATest {

    @InjectMock
    PaymentService paymentService;

    @Inject
    CartService cartService;

    @Inject
    OrderRepository orderRepository;

    @BeforeEach
    public void setup(){
        Mockito.when(paymentService.pay(Mockito.any(CreditCard.class), Mockito.anyLong()))
                .thenReturn(true);
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