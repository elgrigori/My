package gr.aueb.quarkus.shop.application;

import gr.aueb.quarkus.shop.domain.purchase.CreditCard;

public interface PaymentService {
    boolean pay(CreditCard creditCard, Long orderId);

}
