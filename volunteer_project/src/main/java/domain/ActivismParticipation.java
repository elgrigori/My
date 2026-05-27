package domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACTIVISM")
public class ActivismParticipation  extends Participation {

    public ActivismParticipation( Volunteer volunteer1, ActivismAction activism1) {
        super(volunteer1,activism1);  }

    public ActivismParticipation() {

    }


}
