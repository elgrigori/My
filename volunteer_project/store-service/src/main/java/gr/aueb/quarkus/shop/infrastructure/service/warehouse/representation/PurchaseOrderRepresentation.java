package gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation;

import java.util.List;

public class PurchaseOrderRepresentation {

    public Long id;
    public List<StockReservationRepresentation> stockReservations;


}
