package gr.aueb.quarkus.shop.infrastructure.persistence;

import gr.aueb.quarkus.shop.domain.customer.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;

import gr.aueb.quarkus.shop.domain.customer.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

@ApplicationScoped
public class CustomerRepositoryImpl implements PanacheRepository<Customer>, CustomerRepository {
    @Override
    public Optional<Customer> findByEmail(String customerEmail) {
        return find("email", customerEmail).firstResultOptional();
    }

    @Override
    public Optional<Customer> findByCustomerId(Long customerId) {
        return findByIdOptional(customerId);
    }
}