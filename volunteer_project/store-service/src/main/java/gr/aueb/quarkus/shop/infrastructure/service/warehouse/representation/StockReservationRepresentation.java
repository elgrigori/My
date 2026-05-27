package gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation;

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
