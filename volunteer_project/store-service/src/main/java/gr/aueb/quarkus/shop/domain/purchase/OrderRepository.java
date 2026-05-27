package gr.aueb.quarkus.shop.domain.purchase;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> listWithOrderLines();

    Optional<Order> orderWithOrderLines(long orderId);

    void save(Order order);
}
