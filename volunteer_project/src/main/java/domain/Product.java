package domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "requiredItems")
    private int requiredItems;

    @Column(name = "collectedItems")
    private int collectedItems;

    @ManyToOne
    @JoinColumn(name = "action_id", nullable = false)
    private Action action;

    public Product() {
    }

    public Product(String name, int requiredItems, int collectedItems) {
        this.name = name;
        this.requiredItems = requiredItems;
        this.collectedItems = 0;

    }


    // Getters
    public Integer getId() { return id;}
    public int getRequiredItems() { return requiredItems;}
    public int getCollectedItems() { return collectedItems;}
    public String getName() { return name;}
    public Action getAction() { return action;}

    // Setters
    public void setId(Integer id) { this.id = id;  }
    public void setAction(Action action) {  this.action = action; }
    public void setRequiredItems(int requiredItems) {
        if (requiredItems <= 0) {
            throw new DomainException("Τα απαιτούμενα τεμάχια δεν μπορουν να είναι μικρότερα του μηδενος");
        }
        this.requiredItems = requiredItems;
    }

    public void setCollectedItems(int collectedItems) {
        if (collectedItems <= 0) {
            throw new DomainException("Τα Προσφερόμενα τεμάχια δεν μπορουν να είναι μικρότερα του μηδενος");
        }
        this.collectedItems = collectedItems;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("Το όνομα του προϊόντος δεν μπορεί να είναι κενό.");
        }
        this.name = name;
    }
}


