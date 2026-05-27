package gr.aueb.quarkus.warehouse.adapters.in.rest.representation;

public class StockReservationRepresentation {

    public long productId;
    public int quantity;

    public StockReservationRepresentation() {
    }

    public StockReservationRepresentation(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
