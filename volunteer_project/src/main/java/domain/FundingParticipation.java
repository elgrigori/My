package domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.hibernate.annotations.DialectOverride;
import util.SystemDateTime;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("FUNDING")
public class FundingParticipation extends Participation {

    @Column(name = "depositAmount")
    private float depositAmount;

    public FundingParticipation() {
        super();
    }

    public FundingParticipation( Volunteer volunteer, Action action, float depositAmount) {
        super( volunteer, action);
        if(depositAmount <=0)
        {
            throw new DomainException("Το ποσό καταθεσης δεν πρεπει να ειναι μικροτερο του μηδενος");
        }
        this.depositAmount = depositAmount;
    }

    //Getters
    public float getDepositAmount() { return depositAmount; }
    //Setters
    public void setDepositAmount(float depositAmount) {
        if (depositAmount <= 0 ) {
            throw new DomainException("Το χρηματικό ποσό κατάθεσης θα πρέπει να είναι μεγαλύτερο του μηδενός.");
        }
        this.depositAmount = depositAmount;
    }


}
