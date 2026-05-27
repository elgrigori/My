package gr.aueb.quarkus.warehouse.adapters.in.rest.representation;

import java.util.List;

public class PurchaseOrderRepresentation {

    public Long id;
    public List<StockReservationRepresentation> stockReservations;


}
