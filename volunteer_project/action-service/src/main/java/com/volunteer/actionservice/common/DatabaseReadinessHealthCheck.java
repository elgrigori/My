package com.volunteer.actionservice.common;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.Connection;

@Readiness
@ApplicationScoped
public class DatabaseReadinessHealthCheck implements HealthCheck {
    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1)
                    ? HealthCheckResponse.up("action-service-db")
                    : HealthCheckResponse.down("action-service-db");
        } catch (Exception exception) {
            return HealthCheckResponse.named("action-service-db")
                    .down()
                    .withData("error", exception.getMessage())
                    .build();
        }
    }
}
