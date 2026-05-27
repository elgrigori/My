package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("FUNDING")
public class FundingAction extends Action {
    @Column(name = "target_amount")
    private float targetAmount;

    @Column(name = "collected_amount")
    private float collectedAmount;


    public FundingAction() {
        super();
    }

    public FundingAction(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, float targetAmount, Organization organization) {
        super(title, actionDescription, startAt, endAt, organization);
        this.targetAmount = targetAmount;
        this.collectedAmount = 0; // Ξεκινάει από 0
    }

    //Getters
    public float getTargetAmount() {
        return targetAmount;
    }
    public float getCollectedAmount() {
        return collectedAmount;
    }

    //Setters
    public void setTargetAmount(float targetAmount) {
        this.targetAmount = targetAmount;
    }
    public void setCollectedAmount(float amount) {  this.collectedAmount = amount; }

    // Create an Action
    public Action createAction(String title, String actionDescription, LocalDateTime startAt, LocalDateTime endAt, float targetAmount, Organization organization) throws DomainException {

        if(targetAmount < 0)
        {
            throw new DomainException("To απαιτούμενο ποσό  δεν μπορεί να είναι μικρότερο του 0");
        }

        return  new FundingAction(title,actionDescription,startAt,endAt,targetAmount,organization);
    }


    //Create a Funding Participate
    public Participation  createParticipation(Volunteer volunteer, float amount) throws DomainException {

        if(this.targetAmount  < amount + this.collectedAmount){

            throw new DomainException("Το ποσό κατάθεσης υπερβαίνει το απαιτουμενο ποσό δράσης");
        }
        FundingParticipation participation = new FundingParticipation(volunteer,this,amount);
        this.updateCollectedAmount(amount);
        this.addParticipation(participation);
        return participation;

    }

    //Μέθοδος για προσθήκη δωρεάς
    public void updateCollectedAmount(float amount) {
        if (amount < 0) {
            throw new DomainException("Το ποσό χρηματοδότησης δεν μπορει να ειναι αρνητικό");
        }
        if (amount == 0) {
            throw new DomainException("To ποσό χρηματιδότησης δεν μπορεί να είναι 0");
        }
        if (this.getCollectedAmount()+ amount> this.getTargetAmount()) {
            throw new DomainException("Το χρηματικό ποσό κατάθεσης υπερβαίνει το ποσό στόχου.");
        }
        if (this.getActioStatus() == ActionStatus.COMPLETED) {
            throw new DomainException("Η δράση έχει ήδη ολοκληρωθεί. Δεν γίνονται δεκτές άλλες δωρεές.");

        }

        this.collectedAmount += amount;
        updateActionStatus();
    }



    private void updateActionStatus() {
        if (this.collectedAmount >= this.targetAmount) {
            this.setActionStatus(ActionStatus.COMPLETED);
            System.out.println("Η χρηματοδοτική δράση ολοκληρώθηκε επιτυχώς!");
        }
        if (this.collectedAmount < this.targetAmount && this.getEndAt().isBefore(LocalDateTime.now())) {
            this.setActionStatus(ActionStatus.CANCELLED);
            System.out.println("Η δράση χρηματοδότησης έληξε. Το συγκεντρωμένο ποσό διανεμήθηκε στον αποδέκτη.");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FundingAction that = (FundingAction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
