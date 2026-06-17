package com.volunteer.actionservice.adapters.in.rest.representation;

import com.volunteer.actionservice.application.ActionService;
import com.volunteer.actionservice.application.domain.ActionType;
import org.junit.jupiter.api.Test;

import static com.volunteer.actionservice.fixture.ActionFixture.activismAction;
import static com.volunteer.actionservice.fixture.ActionFixture.contributeAction;
import static com.volunteer.actionservice.fixture.ActionFixture.fundingAction;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionResponseMapperTest {
    private final ActionService actionService = new ActionService();

    @Test
    void mapsActivismFields() {
        var response = actionService.toResponse(activismAction("Cleanup", "Athens"));

        assertEquals(ActionType.ACTIVISM, response.type);
        assertEquals("Cleanup", response.title);
        assertEquals("Athens", response.location);
        assertEquals(5, response.minParticipants);
        assertEquals(25, response.totalParticipants);
    }

    @Test
    void mapsFundingFields() {
        var response = actionService.toResponse(fundingAction("School fund"));

        assertEquals(ActionType.FUNDING, response.type);
        assertEquals("School fund", response.title);
        assertEquals(5000, response.targetAmount.intValue());
        assertEquals(150, response.raisedAmount.intValue());
    }

    @Test
    void mapsContributeProducts() {
        var response = actionService.toResponse(contributeAction("Food collection"));

        assertEquals(ActionType.CONTRIBUTE, response.type);
        assertEquals(1, response.products.size());
        assertEquals("Rice", response.products.get(0).name);
        assertEquals(100, response.products.get(0).remainingQuantity);
    }
}
