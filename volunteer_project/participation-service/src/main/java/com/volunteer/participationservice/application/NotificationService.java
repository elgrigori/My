package com.volunteer.participationservice.application;

import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class NotificationService {
    @ConfigProperty(name = "volunteer.notifications.enabled", defaultValue = "true")
    boolean enabled;

    public String confirmation(UserSummary volunteer, ActionSummary action) {
        if (!enabled) {
            return "Notifications disabled";
        }
        return "Confirmation sent to " + volunteer.email + " for action " + action.title;
    }
}
