package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderLine;
import gr.aueb.quarkus.shop.fixture.EntityFixture;
import gr.aueb.quarkus.shop.fixture.RepresentationFixture;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class OrderMapperTest {

    @Inject
    OrderMapper orderMapper;

    @Test
    public void toModel() {

        OrderRepresentation orderRepresentation = RepresentationFixture.OrderFixture.orderWithSingleProduct();
        Order model = orderMapper.toModel(orderRepresentation);

        assertEquals(orderRepresentation.orderLines.size(), model.getOrderLines().size());
        assertEquals(orderRepresentation.customer.id, model.getCustomer().getId());
        assertEquals(LocalDate.of(2021, 1, 1), model.getOrderDate());

        OrderLine orderLine = model.getOrderLines().stream().findFirst().get();
        OrderLineRepresentation orderLineRepresentation = orderRepresentation.orderLines.get(0);
        assertEquals(orderLineRepresentation.quantity, orderLine.getQuantity());
        assertEquals(orderLineRepresentation.product.id, orderLine.getProduct().getId());


    }

    @Test
    public void toRepresentation(){

        Order order = EntityFixture.OrderFixture.newOrderWithSingleProduct();
        OrderRepresentation representation = orderMapper.toRepresentation(order);

        assertEquals(order.getCustomer().getId(), representation.customer.id);
        assertEquals("20210101", representation.createdAt);

        assertEquals(order.getOrderLines().size(), representation.orderLines.size());

        OrderLine orderLine = order.getOrderLines().stream().findFirst().get();
        OrderLineRepresentation orderLineRepresentation = representation.orderLines.get(0);
        assertEquals(orderLine.getQuantity(), orderLineRepresentation.quantity);
        assertEquals(orderLine.getProduct().getId(), orderLineRepresentation.product.id);

    }
}