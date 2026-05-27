package domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@DiscriminatorValue("Organization")
public class Organization extends User {

    @Column(name = "organization_name", length = 100, unique = true)
    private String organizationName;

    @Column(name = "afm", length = 9, unique = true)
    @Pattern(regexp = "^[0-9]{9}$", message = "Invalid AFM. It must be exactly 9 digits.")
    private String afm;

    @Column(name = "description_of_organization", length = 100)
    private String descriptionOfOrganization;

    @Column(name = "description_of_action", length = 100)
    private String descriptionOfAction;


    @Column(name = "year_of_establishment",length = 4)
    private Integer yearOfEstablishment;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Action> actions = new HashSet<Action>();

    public Organization() {}

    public Organization(String username, String password, String email, String mobile, Address address, String organizationName, String afm,
                        String descriptionOfOrganization, String descriptionOfAction, Integer yearOfEstablishment) {
        super(username, password, email, mobile, address);
        this.organizationName = organizationName;
        this.afm = afm;
        this.descriptionOfOrganization = descriptionOfOrganization;
        this.descriptionOfAction = descriptionOfAction;
        this.yearOfEstablishment = yearOfEstablishment;
    }

    //Getters
    public String getOrganizationName() {
        return organizationName;
    }
    public String getAfm() {
        return afm;
    }
    public String getDescriptionOfOrganization() {
        return descriptionOfOrganization;
    }
    public String getDescriptionOfAction() {
        return descriptionOfAction;
    }
    public Integer getYearOfEstablishment() {
        return yearOfEstablishment;
    }
    public Set<Action> getActions() {
        return actions;
    }

    //Setters
    public void setAfm(String afm) {
        this.afm = afm;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    public void setDescriptionOfOrganization(String descriptionOfOrganization) {
        this.descriptionOfOrganization = descriptionOfOrganization;
    }
    public void setDescriptionOfAction(String descriptionOfAction) {
        this.descriptionOfAction = descriptionOfAction;
    }
    public void setYearOfEstablishment(Integer yearOfEstablishment) {
        this.yearOfEstablishment = yearOfEstablishment;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return organizationName.equals(that.organizationName) && afm.equals(that.afm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationName, afm);
    }
}

