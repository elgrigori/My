package gr.aueb.quarkus.shop.infrastructure.persistence;

import gr.aueb.quarkus.shop.common.JPATest;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class OrderRepositoryImplTest extends JPATest {

    @Inject
    OrderRepository orderRepository;

    @Test
    void listWithOrderLines() {

        List<Order> orders = orderRepository.listWithOrderLines();
        assertEquals(1, orders.size());

    }

    @Test
    void findById(){
        Optional<Order> orderOptional = orderRepository.orderWithOrderLines(300l);
        assertTrue(orderOptional.isPresent());
        assertEquals(1, orderOptional.get().getOrderLines().size());
    }

    @Test
    void save() {
    }
}