package com.volunteer.participationservice.fixture;

import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ProductContributionRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import com.volunteer.participationservice.application.domain.Participation;
import com.volunteer.participationservice.application.domain.ParticipationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class ParticipationFixture {
    private ParticipationFixture() {
    }

    public static UserSummary volunteer(Long id) {
        UserSummary user = new UserSummary();
        user.id = id;
        user.type = "VOLUNTEER";
        user.username = "volunteer" + id;
        user.email = "volunteer" + id + "@example.com";
        return user;
    }

    public static ActionSummary action(Long id) {
        return action(id, "ACTIVISM", "Tree planting", LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(30).plusHours(3));
    }

    public static ActionSummary action(Long id, String title, LocalDateTime startDate, LocalDateTime endDate) {
        return action(id, "ACTIVISM", title, startDate, endDate);
    }

    public static ActionSummary action(Long id, String type, String title, LocalDateTime startDate, LocalDateTime endDate) {
        ActionSummary action = new ActionSummary();
        action.id = id;
        action.type = type;
        action.title = title;
        action.startDate = startDate;
        action.endDate = endDate;
        return action;
    }

    public static ParticipationRequest request(Long volunteerId, Long actionId) {
        ParticipationRequest request = new ParticipationRequest();
        request.volunteerId = volunteerId;
        request.actionId = actionId;
        return request;
    }

    public static ParticipationRequest fundingRequest(Long volunteerId, Long actionId, BigDecimal amount) {
        ParticipationRequest request = request(volunteerId, actionId);
        request.type = "FUNDING";
        request.amount = amount;
        return request;
    }

    public static ParticipationRequest contributeRequest(Long volunteerId, Long actionId,
                                                         ProductContributionRequest... products) {
        ParticipationRequest request = request(volunteerId, actionId);
        request.type = "CONTRIBUTE";
        request.products = List.of(products);
        return request;
    }

    public static ProductContributionRequest product(Long productId, String name, Integer quantity) {
        ProductContributionRequest product = new ProductContributionRequest();
        product.productId = productId;
        product.name = name;
        product.quantity = quantity;
        return product;
    }

    public static Participation participation(Long volunteerId, Long actionId, ParticipationStatus status,
                                              LocalDateTime startDate, LocalDateTime endDate) {
        Participation participation = new Participation();
        participation.volunteerId = volunteerId;
        participation.actionId = actionId;
        participation.type = "ACTIVISM";
        participation.startDate = startDate;
        participation.endDate = endDate;
        participation.status = status;
        return participation;
    }
}
