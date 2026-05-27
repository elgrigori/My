package gr.aueb.quarkus.warehouse.application.ports.in;

import gr.aueb.quarkus.warehouse.application.domain.PurchaseOrder;

public interface StockReservationUseCase {
    boolean reserveStock(PurchaseOrder purchaseOrder);
}
