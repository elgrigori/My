package domain;

import jakarta.persistence.*;
import util.SystemDate;
import util.SystemDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "participations")
@DiscriminatorColumn(name = "participation_type", discriminatorType = DiscriminatorType.STRING)
public class Participation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "participationStatus")
    private ParticipationStatus participationStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt = SystemDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "action_id", nullable = false)
    private Action action;


    public Participation() {
    }

    public Participation(Volunteer volunteer, Action action)
    {
        if(volunteer == null || action == null){
            throw new DomainException("Volunteer and Action cannot be null");
        }
        this.participationStatus =ParticipationStatus.ACCEPTED;
        this.volunteer = volunteer;
        this.action = action;
        this.createdAt = SystemDateTime.now();
    }


    //Getters
    public Integer getId() { return id; }
    public ParticipationStatus getParticipationStatus() { return participationStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Volunteer getVolunteer() { return volunteer; }
    public Action getAction() { return action; }


    //Setters
    public void setId(Integer id) { this.id = id; }
    public void setParticipationStatus(ParticipationStatus status) { this.participationStatus = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setVolunteer(Volunteer volunteer) {
        if(volunteer == null){
            throw new DomainException("Πρέπει να υπάρχει εθελοντής");
        }
        this.volunteer = volunteer; }

    public void setAction(Action action) {
        if (action == null ) {
            throw new DomainException("H Δράση δεν μπορεί να είναι κενή.");
        }

        this.action = action;
    }


    //Ακύρωση μια συμμετόχης
    public void cancelParticipation() {
        LocalDateTime now = SystemDateTime.now();
        LocalDateTime actionStartAt = this.action.getStartAt();
        LocalDateTime actionEndAt = this.action.getEndAt();

        if (this.getAction().getActioStatus() == ActionStatus.COMPLETED ||(actionEndAt != null && now.isAfter(actionEndAt))) {
            throw new DomainException("Δεν μπορείς να συμμετάσχεις σε ολοκληρωμένη δράση");
        }

        if (this.getAction().getActioStatus() == ActionStatus.CANCELLED) {
            throw new DomainException("Η Δράση αυτή είναι ήση ακυρωμένη.");
        }

        if (now.isAfter(actionStartAt.minusHours(12))) {

            throw new DomainException("Η ακύρωση της συμμετοχής γίνεται μέχρι 12 ώρες πριν ξεκινήσει η δράση");
        }

        this.participationStatus = ParticipationStatus.CANCELLED;
    }

}

