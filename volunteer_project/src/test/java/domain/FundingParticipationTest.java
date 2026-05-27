package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SystemDateTime;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FundingParticipationTest {
    Volunteer volunteer ;
    Address address;
    FundingAction action;
    LocalDateTime now =LocalDateTime.now();
    FundingParticipation participation;

    @BeforeEach
    void setUp() {
        SystemDateTime.setStub(now);
        address = new Address() ;
        address.setStreet("Iras");
        address.setStreetNumber(38);
        address.setCity("Athens");
        address.setPostalCode("11146");

        // Create a volunteer
        volunteer = new Volunteer();
        volunteer.setUsername("elenol_88");
        volunteer.setPassword("Password123!");
        volunteer.setFirstName("Eleni");
        volunteer.setLastName("Grigori");
        volunteer.setEmail("eleni@test.com");
        volunteer.setMobile("685212458");
        volunteer.setAddress(address);

        action = new FundingAction();
        action.setTitle("New Action");
        action.setActionDescription("This is an action");
        action.setTargetAmount(1500);
        action.setStartAt(now.plusDays(1));
        action.setEndAt(now.plusDays(2));



        participation = new FundingParticipation();
        participation.setAction(action);
        participation.setVolunteer(volunteer);
        participation.setCreatedAt(now);

    }

    @Test
    public void testFundingParticipationConstructor() {

        // Create an instance of FundingParticipation
        FundingParticipation fundingParticipation = new FundingParticipation(
                ParticipationStatus.ACCEPTED, volunteer, action, 10.50f
        );

        // Validate fields
        assertEquals(fundingParticipation.getParticipationStatus(),ParticipationStatus.ACCEPTED);
        assertEquals(fundingParticipation.getVolunteer(),volunteer);
        assertEquals(fundingParticipation.getCreatedAt(),now);
        assertEquals(fundingParticipation.getDepositAmount(),10.50f);
    }

    @Test
    public void denyFundingParticipationConstructor() {
        assertThrows(DomainException.class, () -> {
            FundingParticipation fundingParticipation = new FundingParticipation(
                    ParticipationStatus.ACCEPTED, null, action, 10.50f
            );

        });
        assertThrows(DomainException.class, () -> {
            FundingParticipation fundingParticipation = new FundingParticipation(
                    ParticipationStatus.ACCEPTED, volunteer, null, 10.50f
            );

        });
        assertThrows(DomainException.class, () -> {
            FundingParticipation fundingParticipation = new FundingParticipation(
                    ParticipationStatus.ACCEPTED, volunteer, action, 0
            );

        });
        assertThrows(DomainException.class, () -> {
            FundingParticipation fundingParticipation = new FundingParticipation(
                    ParticipationStatus.ACCEPTED, volunteer, action, -1
            );

        });
    }

    @Test
    void shouldThrowExceptionWhenSettingNegativeAmount() throws Exception {
        assertThrows(DomainException.class, () -> {
            participation.setDepositAmount(-100);

        });
    }
    @Test
    void shouldThrowExceptionWhenSettingZero() throws Exception {
        assertThrows(DomainException.class, () -> {
            participation.setDepositAmount(0);

        });
    }

    @Test
    void allowSettingDepositAmount() throws Exception {
        participation.setDepositAmount(10);
        assertEquals(10, participation.getDepositAmount());

    }

    @Test
    void allowAddingDepositAmount() throws Exception {
        action.setCollectedAmount(1000);

        participation.addDeposit(200);
        assertEquals(200., participation.getDepositAmount());
        assertEquals(1200, action.getCollectedAmount());
    }

    @Test
    void denyAddingDepositAmount() throws Exception {

        assertThrows(DomainException.class, () -> {
            action.setCollectedAmount(1000);
            participation.addDeposit(-9);

        });

        assertThrows(DomainException.class, () -> {
            ContributeAction contributeAction = new ContributeAction();

            participation.setAction(contributeAction);
            participation.addDeposit(200);

        });
        assertThrows(DomainException.class, () -> {
            action.setCollectedAmount(1000);
            participation.setAction(action);
            participation.addDeposit(2000);

        });
    }
}
