package com.volunteer.participationservice.service;

import com.volunteer.participationservice.dto.ActionSummary;
import com.volunteer.participationservice.dto.UserSummary;
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
