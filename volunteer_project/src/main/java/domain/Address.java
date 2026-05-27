package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(name = "street", length = 100, nullable = false)
    private String street;

    @Column(name = "street_number", length = 3, nullable = false)
    private Integer streetNumber;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postalCode;

    @Column(name = "city", length = 50, nullable = false)
    private String city;


    public Address() {
    }

    public Address(String street, Integer streetNumber, String postalCode, String city) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
    }

    // Getters
    public String getStreet() {
        return street;
    }
    public Integer getStreetNumber() {
        return streetNumber;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getCity() {
        return city;
    }


    //Setters
    public void setStreet(String street) {
        this.street = street;
    }
    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setCity(String city) {
        this.city = city;
    }


}



