package com.volunteer.actionservice.fixture;

import com.volunteer.actionservice.adapters.in.rest.representation.ActionRequest;
import com.volunteer.actionservice.adapters.in.rest.representation.ProductRequest;
import com.volunteer.actionservice.application.domain.ActionProduct;
import com.volunteer.actionservice.application.domain.ActionType;
import com.volunteer.actionservice.application.domain.ActivismAction;
import com.volunteer.actionservice.application.domain.ContributeAction;
import com.volunteer.actionservice.application.domain.FundingAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class ActionFixture {
    private ActionFixture() {
    }

    public static ActivismAction activismAction(String title, String location) {
        ActivismAction action = new ActivismAction();
        action.organizationId = 15L;
        action.title = title;
        action.description = "Community activism";
        action.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        action.endDate = LocalDateTime.of(2026, 7, 1, 14, 0);
        action.location = location;
        action.category = "environment";
        action.minParticipants = 5;
        action.maxParticipants = 25;
        return action;
    }

    public static FundingAction fundingAction(String title) {
        FundingAction action = new FundingAction();
        action.organizationId = 15L;
        action.title = title;
        action.description = "Raise money";
        action.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        action.endDate = LocalDateTime.of(2026, 7, 31, 18, 0);
        action.location = "Athens";
        action.category = "funding";
        action.targetAmount = BigDecimal.valueOf(5000);
        action.raisedAmount = BigDecimal.valueOf(150);
        return action;
    }

    public static ContributeAction contributeAction(String title) {
        ContributeAction action = new ContributeAction();
        action.organizationId = 15L;
        action.title = title;
        action.description = "Collect goods";
        action.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        action.endDate = LocalDateTime.of(2026, 7, 15, 18, 0);
        action.location = "Athens";
        action.category = "food";
        action.products.add(product("Rice", 100));
        return action;
    }

    public static ActionRequest activismRequest(String title, String location) {
        ActionRequest request = new ActionRequest();
        request.type = ActionType.ACTIVISM;
        request.organizationId = 15L;
        request.title = title;
        request.description = "Community activism";
        request.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        request.endDate = LocalDateTime.of(2026, 7, 1, 14, 0);
        request.location = location;
        request.category = "environment";
        request.minParticipants = 5;
        request.maxParticipants = 25;
        return request;
    }

    public static ActionRequest fundingRequest(String title, BigDecimal targetAmount) {
        ActionRequest request = new ActionRequest();
        request.type = ActionType.FUNDING;
        request.organizationId = 15L;
        request.title = title;
        request.description = "Raise money";
        request.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        request.endDate = LocalDateTime.of(2026, 7, 31, 18, 0);
        request.location = "Athens";
        request.targetAmount = targetAmount;
        return request;
    }

    public static ActionRequest contributeRequest(String title, ProductRequest... products) {
        ActionRequest request = new ActionRequest();
        request.type = ActionType.CONTRIBUTE;
        request.organizationId = 15L;
        request.title = title;
        request.description = "Collect goods";
        request.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        request.endDate = LocalDateTime.of(2026, 7, 15, 18, 0);
        request.location = "Athens";
        request.products = List.of(products);
        return request;
    }

    public static ProductRequest productRequest(String name, Integer targetQuantity) {
        ProductRequest request = new ProductRequest();
        request.name = name;
        request.targetQuantity = targetQuantity;
        return request;
    }

    public static ActionProduct product(String name, Integer targetQuantity) {
        ActionProduct product = new ActionProduct();
        product.name = name;
        product.targetQuantity = targetQuantity;
        product.remainingQuantity = targetQuantity;
        return product;
    }
}
