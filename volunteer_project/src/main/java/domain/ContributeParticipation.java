package domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Entity
@DiscriminatorValue("CONTRIBUTE")
public class ContributeParticipation extends  Participation {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "participation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParticipationProduct> participationProducts = new HashSet<>();


    public ContributeParticipation() {}

    public ContributeParticipation(Volunteer volunteer, Action action, Map<Product, Integer> offeredQuantities) {
        super(volunteer, action); // Call to parent class constructor (Participation)

        if (offeredQuantities == null || offeredQuantities.isEmpty()) {
            throw new DomainException("Πρέπει να καθοριστούν ποσότητες για τουλάχιστον ένα προϊόν.");
        }

        // Initialize participationProducts from the offeredQuantities map
        for (Map.Entry<Product, Integer> entry : offeredQuantities.entrySet()) {
            Product product = entry.getKey();
            int offeredQuantity = entry.getValue();

            if (product == null) {
                throw new DomainException("Το προϊόν δεν μπορεί να είναι κενό");
            }
            if (offeredQuantity <= 0) {
                throw new DomainException("Η προσφερόμενη ποσότητα πρέπει να είναι μεγαλύτερη από το μηδέν.");
            }


            ParticipationProduct participationProduct = new ParticipationProduct(this, product, offeredQuantity);
            this.participationProducts.add(participationProduct);
        }
    }

    // Getters
    public Integer getId() {return id;  }
    public Set<ParticipationProduct> getParticipationProducts(){return participationProducts;}

    // Setters
    public void setId(int id) {this.id = id;}
    public void setParticipationProducts(Set<ParticipationProduct> participationProducts) {
        this.participationProducts = participationProducts;
    }

    // Προσθηκη ενος προιοντος με την συμμετοχη
    public void addProduct(Product product , int offeredQuantity ) {
        if(product == null) {
            throw new DomainException("Το προιον δεν μπορει να ειναι κενο");
        }
        if(offeredQuantity < 0)
        {
            throw new DomainException("Η προσφερόμενη ποσοτητα δεν μπορεί να είναι αρνητική");
        }
        ParticipationProduct participationProduct = new ParticipationProduct(this, product, offeredQuantity);
        participationProducts.add(participationProduct);
        participationProduct.setParticipation(this);

    }

    // Αφαιρεση ενος προιοντος απο την σμμετοχη
    public void removeProduct(Product product) {
        if (product == null) {
            throw new DomainException("Το προιον δεν μπορεί να είναι κενό.");
        }

        ParticipationProduct productToRemove = null;

        for (ParticipationProduct pp : participationProducts) {
            if (pp.getProduct().equals(product)) {
                productToRemove = pp;
                break;
            }
        }

        if (productToRemove == null) {
            throw new DomainException("Αυτο το προιον δεν συνδεεται με αυτη τη συμμετοχη.");
        }

        participationProducts.remove(productToRemove);
        productToRemove.setParticipation(null);
    }

}


