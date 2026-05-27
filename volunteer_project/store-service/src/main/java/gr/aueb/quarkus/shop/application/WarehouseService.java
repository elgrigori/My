package gr.aueb.quarkus.shop.application;

import gr.aueb.quarkus.shop.domain.purchase.Order;

public interface WarehouseService {

    boolean reserveStockFor(Order order);
}
