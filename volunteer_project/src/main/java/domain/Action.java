package domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import jakarta.persistence.*;
import util.SystemDateTime;

import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "action")
@DiscriminatorColumn(name = "action_type", discriminatorType = DiscriminatorType.STRING)
public class Action {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "title",nullable = false, length = 100)
    private String title;

    @Column(name = "action_description",nullable = false)
    private String actionDescription;

    @Column(name = "start_at",nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at",nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_status",nullable = false)
    private ActionStatus actionStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Participation> participations = new HashSet<>();
    

    public Action(){
    }

    public Action(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, Organization organization) {
        this.title = title;
        this.actionDescription = actionDescription;
        this.startAt = startAt;
        this.endAt = endAt;
        this.actionStatus = ActionStatus.OPEN;
        this.organization = organization;
    }

    //getters
    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getActionDescription() {
        return actionDescription;
    }
    public LocalDateTime getStartAt() {
        return startAt;
    }
    public LocalDateTime getEndAt() {
        return endAt;
    }
    public ActionStatus getActioStatus() {
        return actionStatus;
    }
    public Organization getOrganization() {
        return organization;
    }
    public Set<Participation> getParticipations() {
        return participations;
    }



    //Setters
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }
    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }
    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
    public void setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }
    public void setParticipations(Set<Participation> participations) {
        this.participations = participations;
    }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public void addParticipation(Participation participation) {
        participation.setAction(this); // Ensure the bidirectional relationship
    }

    public void removeParticipation(Participation participation) {
        participations.remove(participation);
        participation.setAction(null); // Ensure proper disassociation
    }

    public Participation  createParticipation(Volunteer volunteer) {
        if (volunteer != null) {
            throw new DomainException("Ο εθελοντής δεν μπορει να είναι κενος");
        }

        if (this.getActioStatus() == ActionStatus.CANCELLED) {
            throw new DomainException("Δεν μπορείς να λάβεις μέρος σε μια ακυρωμένη δράση.");
        }

        if (this.getActioStatus() == ActionStatus.COMPLETED) {
            throw new DomainException("Δεν μπορείς να λάβεις μέρος σε μια ολοκληρωμένη δράση.");
        }
        Participation participation = new Participation(volunteer, this);
        return participation;
    }

    public void cancelAction() throws DomainException {
        if (this ==  null) {
            throw  new DomainException("No action");
        }

        LocalDateTime actionStartAt = this.getStartAt();
        LocalDateTime actionEndAt = this.getEndAt();
        LocalDateTime now = SystemDateTime.now();

        // Check if action has already ended
        if(this.actionStatus == ActionStatus.CANCELLED){
            throw new DomainException("Η Δράση είναι ήση ακυρωμένη'");
        }
        if (actionEndAt != null && now.isAfter(actionEndAt)) {
            throw new DomainException("Η Δράση είναι ολοκληρωμένη");
        }
        if (now.isAfter(actionStartAt.minusHours(12))) {
            throw new DomainException("Μια δράση επιτρέπεται να ακυρωθεί μεχρι 12 ώρες πρίν την έναρξή της");
        }

        this.actionStatus = ActionStatus.CANCELLED ;
        for (Participation participation : this.getParticipations()) {
            Volunteer volunteer = participation.getVolunteer();
            sendNotificationToVolunteer(volunteer, this);
        }

    }
    // Ειδοποίηση οτι η δράση έχει ακυρωθεί.
    private void sendNotificationToVolunteer(Volunteer volunteer, Action action) {

        String message = "Αγαπητέ/ή " + volunteer.getFirstName() + ",η Δράση '" + action.getTitle() + "' στην οποίο έχετε δηλώσει συμμετοχή έχει ακυρωθεί";

        Notification notification = new Notification(message, volunteer);
        volunteer.addNotification(notification);

    }
    public void completeAction() {
        if (this == null) {
            throw new DomainException("No action found.");
        }


        if (this.actionStatus == ActionStatus.COMPLETED) {
            throw new DomainException("Action is already completed.");
        }
        if (this.actionStatus == ActionStatus.CANCELLED) {
            throw new DomainException("Cancelled actions cannot be completed.");
        }

        LocalDateTime now = SystemDateTime.now();
        if (this.endAt != null && now.isBefore(this.endAt)) {
            throw new DomainException("Action cannot be completed before its deadline.");
        }
        this.actionStatus = ActionStatus.COMPLETED;
    }

    public boolean isOngoing() {
        LocalDateTime now = SystemDateTime.now();
        return now.isAfter(startAt) && now.isBefore(endAt);
    }

    public boolean isExpired() {
        return SystemDateTime.now().isAfter(endAt);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action that = (Action) o;
        return getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }

}
