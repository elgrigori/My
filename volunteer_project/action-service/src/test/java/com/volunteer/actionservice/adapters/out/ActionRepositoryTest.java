package com.volunteer.actionservice.adapters.out;

import com.volunteer.actionservice.IntegrationBase;
import com.volunteer.actionservice.application.domain.ActionType;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class ActionRepositoryTest extends IntegrationBase {
    @Test
    void seedsDesignActions() {
        var actions = actionRepository.listAll();

        assertEquals(3, actions.size());
        assertTrue(actions.stream().anyMatch(action -> action.type() == ActionType.ACTIVISM));
        assertTrue(actions.stream().anyMatch(action -> action.type() == ActionType.CONTRIBUTE));
        assertTrue(actions.stream().anyMatch(action -> action.type() == ActionType.FUNDING));
    }

    @Test
    void findsSeedActionByTitle() {
        var action = actionRepository.find("title", "Beach cleanup").firstResultOptional();

        assertTrue(action.isPresent());
        assertEquals(ActionType.ACTIVISM, action.get().type());
    }

    @Test
    void contributeSeedKeepsProducts() {
        var action = actionRepository.find("title", "Food collection").firstResultOptional();

        assertTrue(action.isPresent());
        assertEquals(ActionType.CONTRIBUTE, action.get().type());
    }
}
