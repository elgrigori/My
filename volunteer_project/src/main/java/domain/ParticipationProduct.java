package domain;

import jakarta.persistence.*;
import util.SystemDate;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "participation_products")
public class ParticipationProduct {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name = "offered_quantity", nullable = false)
    private int offeredQuantity;
    @Column(name="created_at", nullable = false)
    private LocalDate createdAt = SystemDate.now();

    public ParticipationProduct() {}

    public ParticipationProduct(ContributeParticipation participation, Product product , int offeredQuantity) {
        if (offeredQuantity <= 0) {
            throw new DomainException("Η προσφερόμενη ποσότητα πρέπει να είναι θετικός αριθμός.");
        }
        this.participation = participation;
        this.product = product;
        this.offeredQuantity = offeredQuantity;
        this.createdAt = LocalDate.now();
    }
    //getters
    public Integer getId() { return id;}
    public Participation getParticipation() {return participation;}
    public Product getProduct(){return product;}
    public LocalDate getCreatedAt(){return createdAt;}
    public int getOfferedQuantity() { return offeredQuantity; }
    //setters

    public void setParticipation(Participation participation) {this.participation =participation;}
    public void setProduct(Product product) { this.product = product;}
    public void setCreatedAt(LocalDate date) {this.createdAt =date;}
    public void setOfferedQuantity(int offeredQuantity) {
        if (offeredQuantity <= 0) {
            throw new DomainException("Η προσφερόμενη ποσότητα πρέπει να είναι θετικός αριθμός.");
        }
        if(this.product.getCollectedItems() + offeredQuantity > this.product.getRequiredItems())
        {
            throw new DomainException("Μπορείς να προσφέρεις μέχρι: " + (this.product.getRequiredItems() - this.product.getCollectedItems()));
        }

        this.offeredQuantity = offeredQuantity;
    }
}
