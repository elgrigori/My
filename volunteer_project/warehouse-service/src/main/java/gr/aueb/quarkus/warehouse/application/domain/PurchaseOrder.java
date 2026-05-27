package gr.aueb.quarkus.warehouse.application.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    private Long id;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL)
    Set<StockReservation> stockReservations = new HashSet<>();

    public PurchaseOrder() {
    }

    public PurchaseOrder(Long id, LocalDate purchaseDate) {
        this.id = id;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Set<StockReservation> getStockReservations() {
        return stockReservations;
    }

    public void addStockReservation(Product product, int quantity){

    }
}
