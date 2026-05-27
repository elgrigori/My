package gr.aueb.quarkus.shop.domain.customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByEmail(String customerEmail);

    Optional<Customer> findByCustomerId(Long customerId);
}
