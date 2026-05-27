package domain;

import jakarta.persistence.*;
import util.SystemDateTime;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("VOLUNTEER")
public class Volunteer extends User {

    @Column(name = "first_name", length = 50, unique = true)
    private String firstName;

    @Column(name = "last_name", length = 50, unique = true)
    private String lastName;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    public Volunteer() {
    }

    public Volunteer(String username, String password, String email, Address address, String mobile, String firstName, String lastName) {
        super(username, password, email, mobile, address);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }


    //Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    //Προσθήκε μιας ειδοποίησης σε εναν εθελοντη
    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

}

