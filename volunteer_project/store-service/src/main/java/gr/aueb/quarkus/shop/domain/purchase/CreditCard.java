package gr.aueb.quarkus.shop.domain.purchase;

public class CreditCard {

    private String cardHolder;
    private String cardNumber;
    private String cvv;
    private int expirationYear;
    private int expirationMonth;

    public CreditCard(String cardHolder, String cardNumber, String cvv, int expirationYear, int expirationMonth) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationYear = expirationYear;
        this.expirationMonth = expirationMonth;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public int getExpirationMonth() {
        return expirationMonth;
    }
}
