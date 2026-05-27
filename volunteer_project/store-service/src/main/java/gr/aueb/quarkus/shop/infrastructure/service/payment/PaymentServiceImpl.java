package gr.aueb.quarkus.shop.infrastructure.service.payment;

import gr.aueb.quarkus.shop.application.PaymentService;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@RequestScoped
public class PaymentServiceImpl implements PaymentService {

    @Inject
    Logger logger;

    @Override
    public boolean pay(CreditCard creditCard, Long orderId) {
        logger.info("Submitted payment for order id:" + orderId);
        return true;
    }
}
