package gr.aueb.quarkus.shop.domain.purchase;

import jakarta.persistence.*;

import java.time.LocalDate;

@Embeddable
public class Payment {

    @Column
    private LocalDate paymentDate;

    @Column(name = "amount")
    private double amount;

    public Payment(LocalDate paymentDate, double amount) {
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    private Payment() {
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
