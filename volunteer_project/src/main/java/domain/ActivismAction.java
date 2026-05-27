package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@DiscriminatorValue("HACTIVISM")
public class ActivismAction extends Action {

    @Column(name = "location")
    private String location;
    @Column(name = "minParticipants")
    private int minParticipants;
    @Column(name = "totalParticipants")
    private int totalParticipants;
    @Column(name = "collectedParticipants")
    private int collectedParticipants;

    public ActivismAction() {
        super();
    }

    public ActivismAction(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, String location, int minParticipants, int totalParticipants, Organization organization) {
        super(title, actionDescription, startAt, endAt, organization);
        this.location = location;
        this.minParticipants = minParticipants;
        this.totalParticipants = totalParticipants;
        this.collectedParticipants = 0;
    }


    //Getters
    public String getLocation() {
        return location;
    }
    public int getMinParticipants() {
        return minParticipants;
    }
    public int getTotalParticipants() {
        return totalParticipants;
    }
    public int getCollectedParticipants() {
        return collectedParticipants;
    }

    //Setters
    public void setLocation(String location) {
        this.location = location;
    }
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }
    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }
    public void setCollectedParticipants(int collectedParticipants) {
        this.collectedParticipants = collectedParticipants;
    }


    public void addParticipant() throws DomainException {
        if(this.getActioStatus() == ActionStatus.COMPLETED)
        {
            throw new DomainException(("Η δράση είναι ολοκληρωμένη."));
        }
        if(this.getActioStatus() == ActionStatus.CANCELLED)
        {
            throw new DomainException(("Η δράση είναι ακυρωμένη."));
        }
        if (this.collectedParticipants < this.totalParticipants) {
            this.collectedParticipants++;

        }
        else {
            throw new DomainException("Δεν υπάρχουν διαθέσιμες θέσεις για αυτή τη δράση.");

        }
    }

    // Δημιουργία μια συμμετοχης ακτιβισμου
    public Participation createParticipation(Volunteer volunteer) throws DomainException {

        if(this.totalParticipants > this.collectedParticipants + 1)
        {
            throw new DomainException("Ο απαιτούμενος αριθμός έχει συμπληρωθει.");

        }
        ActivismParticipation participation = new ActivismParticipation(volunteer,this);

        this.addParticipation(participation);
        this.addParticipant();
        return participation;
    }

    public Action createAction(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, String location, int minParticipants, int totalParticipants, Organization organization) throws DomainException {
        if(organization == null){
            throw new DomainException("Ο οργανισμός δεν μπορεί να είναι κενός.");
        }
        if(location == null)
        {
            throw new DomainException("Η τοποθεσία πρέπει να μην είνια κενή");
        }
        if(minParticipants < 0 ){
            throw new DomainException("Ο ελάχιστος αριθμός συμμετέχοντων δεν μπορεί να είναι μικρότερος του 0");
        }
        if(totalParticipants < 0 ){
            throw new DomainException("Ο απαιτούμενος αριθμός συμμετέχοντων δεν μπορεί να είναι μικρότερος του 0");
        }
        return  new ActivismAction(title,actionDescription,startAt,endAt,location,minParticipants,totalParticipants,organization);
    }

    @Override
    public void cancelAction() throws DomainException {
        if (this.collectedParticipants >= this.minParticipants ) {
            throw new DomainException("Η δράση δεν μπορεί να ακυρωθεί λογώ επαρκων συμμετοχώ.");
        }
        if (this.getEndAt().isBefore(LocalDateTime.now())) {
            throw new DomainException("Δεν μπορείτε να ακυρώσετε μια ολοκληρωμένη δράση.");
        }

            this.setActionStatus(ActionStatus.CANCELLED);

        }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ActivismAction activismAction = (ActivismAction) o;
        return minParticipants == activismAction.minParticipants &&
                totalParticipants == activismAction.totalParticipants &&
                collectedParticipants == activismAction.collectedParticipants &&
                Objects.equals(location, activismAction.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, minParticipants, totalParticipants, collectedParticipants);
    }
}
