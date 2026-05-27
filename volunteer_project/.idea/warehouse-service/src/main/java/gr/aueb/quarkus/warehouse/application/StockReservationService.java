package gr.aueb.quarkus.warehouse.application;

import gr.aueb.quarkus.warehouse.application.domain.PurchaseOrder;
import gr.aueb.quarkus.warehouse.application.ports.in.StockReservationUseCase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class StockReservationService implements StockReservationUseCase {

    @Override
    public boolean reserveStock(PurchaseOrder purchaseOrder) {
        return true;
    }
}
