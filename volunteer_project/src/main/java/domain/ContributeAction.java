package domain;

import jakarta.persistence.*;
import java.util.Map;

import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@DiscriminatorValue("CONTRIBUTE")
@Table(name = "contribute_action")
public class ContributeAction extends Action {

    @Column(name = "location", length = 255)
    private String location;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    public ContributeAction() {
        super();
    }

    public ContributeAction(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, String location, Organization organization, Set<Product> products) {
        super(title, actionDescription, startAt, endAt, organization);
        this.location = location;
        this.products = products != null ? products : new HashSet<>();
    }

    // Getters
    public String getLocation() { return location;  }
    public Set<Product> getProducts() { return products; }

    // Setters
    public void setLocation(String location) {  this.location = location;  }
    public void setProducts(Set<Product> products) { this.products = products; }

    // Προσθήκς Προιοντος στη δράση
    public void addProduct(Product product) {
        if (product == null) {
            throw new DomainException("Το προϊόν δεν μπορεί να είναι κενό.");
        }
        products.add(product);
        product.setAction(this);
    }

    // Κατάργηση ενος προιοντος απο τη δράση
    public void removeProduct(Product product) {
        if (!products.contains(product)) {
            throw new DomainException("Το προϊόν δεν βρέθηκε στη δράση.");
        }
        products.remove(product);
        product.setAction(null);
    }

    // Δημιουργια μια δρασης προσφορας
    public Participation createParticipation(Volunteer volunteer, Map<Product, Integer> offeredQuantities) {
        if (volunteer == null) {
            throw new DomainException("Ο εθελοντής δεν μπορεί να είναι κενός.");
        }
        if (offeredQuantities == null || offeredQuantities.isEmpty()) {
            throw new DomainException("Πρέπει να καθοριστούν ποσότητες για τουλάχιστον ένα προϊόν.");
        }

        // Ensure the products in the map exist in this action
        for (Product product : offeredQuantities.keySet()) {
            if (!products.contains(product)) {
                throw new DomainException("Το προϊόν " + product.getName() + " δεν ανήκει σε αυτή τη δράση.");
            }
        }
        ContributeParticipation participation = new ContributeParticipation(volunteer, this, offeredQuantities);
        this.addParticipation(participation);
        return  participation;
    }


    //Δημιουργία δράσης προσφοράς
    public Action createAction(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, String location, Organization organization, Set<Product> products) throws DomainException {
        if(organization == null){
            throw new DomainException("Ο οργανισμός δεν μπορεί να είναι κενός.");
        }
        if (location == null) {
            throw new DomainException("Η τοποθεσία πρέπει να μην είνια κενή");
        }
        if (products == null) {
            throw new DomainException("Η δράση πρέπει να περιέχει τουλάχστον ένα προιον");
        }

        ContributeAction action = new ContributeAction(title, actionDescription, startAt, endAt, location, organization, products);
        for (Product product : products) { // foreach loop
            product.setAction(ContributeAction.this);
        }
        return action;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContributeAction that = (ContributeAction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
