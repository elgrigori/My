package com.volunteer.actionservice;

import com.volunteer.actionservice.adapters.out.ActionRepository;
import com.volunteer.actionservice.application.domain.ActionProduct;
import com.volunteer.actionservice.application.domain.ActivismAction;
import com.volunteer.actionservice.application.domain.ContributeAction;
import com.volunteer.actionservice.application.domain.FundingAction;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public abstract class IntegrationBase {
    protected static final Long DESIGN_ORGANIZATION_ID = 15L;

    @Inject
    protected ActionRepository actionRepository;

    @Inject
    protected EntityManager entityManager;

    @BeforeEach
    @Transactional
    protected void initDb() {
        entityManager.createNativeQuery("delete from action_products").executeUpdate();
        actionRepository.deleteAll();
        actionRepository.persist(designActivismAction());
        actionRepository.persist(designContributeAction());
        actionRepository.persist(designFundingAction());
    }

    private ActivismAction designActivismAction() {
        ActivismAction action = new ActivismAction();
        action.organizationId = DESIGN_ORGANIZATION_ID;
        action.title = "Beach cleanup";
        action.description = "Volunteer beach cleanup";
        action.startDate = LocalDateTime.of(2026, 6, 10, 9, 0);
        action.endDate = LocalDateTime.of(2026, 6, 10, 13, 0);
        action.location = "Alimos";
        action.category = "community";
        action.minParticipants = 10;
        action.maxParticipants = 40;
        return action;
    }

    private ContributeAction designContributeAction() {
        ContributeAction action = new ContributeAction();
        action.organizationId = DESIGN_ORGANIZATION_ID;
        action.title = "Food collection";
        action.description = "Collect basic goods";
        action.startDate = LocalDateTime.of(2026, 6, 1, 10, 0);
        action.endDate = LocalDateTime.of(2026, 6, 15, 18, 0);
        action.location = "Athens";
        action.category = "community";
        action.products.add(product("Rice", 100));
        action.products.add(product("Milk", 80));
        return action;
    }

    private FundingAction designFundingAction() {
        FundingAction action = new FundingAction();
        action.organizationId = DESIGN_ORGANIZATION_ID;
        action.title = "Community pantry support";
        action.description = "Collect money";
        action.startDate = LocalDateTime.of(2026, 6, 1, 10, 0);
        action.endDate = LocalDateTime.of(2026, 6, 30, 18, 0);
        action.location = "Athens";
        action.category = "community";
        action.targetAmount = BigDecimal.valueOf(5000.0);
        action.raisedAmount = BigDecimal.ZERO;
        return action;
    }

    private ActionProduct product(String name, Integer targetQuantity) {
        ActionProduct product = new ActionProduct();
        product.name = name;
        product.targetQuantity = targetQuantity;
        product.remainingQuantity = targetQuantity;
        return product;
    }
}
