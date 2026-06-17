package com.volunteer.actionservice.application.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionTest {
    @Test
    void activismTypeIsActivism() {
        assertEquals(ActionType.ACTIVISM, new ActivismAction().type());
    }

    @Test
    void contributeTypeIsContribute() {
        assertEquals(ActionType.CONTRIBUTE, new ContributeAction().type());
    }

    @Test
    void fundingTypeIsFunding() {
        assertEquals(ActionType.FUNDING, new FundingAction().type());
    }
}
