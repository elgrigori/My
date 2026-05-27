package domain;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;


    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;


    public Notification() {
    }

    public Notification(String description, Volunteer volunteer) {
        this.description = description;
        this.volunteer = volunteer;
        this.createdAt = LocalDateTime.now();
    }

    //Getters
    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public Volunteer getVolunteer() {  return volunteer;}
    public LocalDateTime getCreatedAt() {return createdAt;}

    //Setters
    public void setDescription(String description) {   this.description = description; }

    public void setVolunteer(Volunteer volunteer) {  this.volunteer = volunteer; }

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt; }

}
