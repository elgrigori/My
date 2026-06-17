package com.volunteer.participationservice;

import com.volunteer.participationservice.adapters.out.ActionClient;
import com.volunteer.participationservice.adapters.out.UserClient;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class ParticipationResourceTest extends IntegrationBase {
    @InjectMock
    @RestClient
    UserClient userClient;

    @InjectMock
    @RestClient
    ActionClient actionClient;

    @Test
    void createsListsAndCancelsParticipation() {
        when(userClient.getUser(101L)).thenReturn(volunteer(101L));
        when(actionClient.getAction(201L)).thenReturn(action(201L));

        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 101,
                          "actionId": 201
                        }
                        """)
                .when()
                .post("/participations")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("CONFIRMED"))
                .extract()
                .path("id");

        given()
                .get("/participations/volunteer/{id}", 101)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        given().get("/participations/{id}", id)
                .then()
                .statusCode(200)
                .body("volunteerId", equalTo(101))
                .body("actionId", equalTo(201));

        given().get("/volunteers/{id}/participations", 101)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        given().get("/actions/{id}/participations", 201)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));

        given().patch("/notifications/{id}/read", id)
                .then()
                .statusCode(200)
                .body("notificationRead", equalTo(true));

        given().delete("/participations/{id}", id).then().statusCode(204);
        given().get("/participations/volunteer/{id}", 101).then().statusCode(200).body("$", hasSize(0));
    }

    @Test
    void returns409WhenActionNotOpen() {
        UserSummary vol = volunteer(102L);
        ActionSummary cancelled = action(202L);
        cancelled.status = "CANCELLED";
        when(userClient.getUser(102L)).thenReturn(vol);
        when(actionClient.getAction(202L)).thenReturn(cancelled);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 102,
                          "actionId": 202
                        }
                        """)
                .when()
                .post("/participations")
                .then()
                .statusCode(409);
    }

    @Test
    void returns409WhenActionUnavailable() {
        ActionSummary fullAction = action(206L);
        fullAction.available = false;
        when(userClient.getUser(106L)).thenReturn(volunteer(106L));
        when(actionClient.getAction(206L)).thenReturn(fullAction);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 106,
                          "actionId": 206
                        }
                        """)
                .when()
                .post("/participations")
                .then()
                .statusCode(409);
    }

    @Test
    void returns409WhenVolunteerHasOverlappingParticipation() {
        ActionSummary morning = action(207L);
        morning.startDate = LocalDateTime.now().plusDays(20);
        morning.endDate = morning.startDate.plusHours(3);
        ActionSummary overlapping = action(208L);
        overlapping.startDate = morning.startDate.plusHours(1);
        overlapping.endDate = morning.startDate.plusHours(4);
        when(userClient.getUser(107L)).thenReturn(volunteer(107L));
        when(actionClient.getAction(207L)).thenReturn(morning);
        when(actionClient.getAction(208L)).thenReturn(overlapping);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 107,
                          "actionId": 207
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 107,
                          "actionId": 208
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(409);
    }

    @Test
    void returns404WhenCancellingUnknownParticipation() {
        given().patch("/participations/{id}/cancel", 9999)
                .then()
                .statusCode(404);
    }

    @Test
    void returns409WhenCancellingAlreadyCancelledParticipation() {
        when(userClient.getUser(108L)).thenReturn(volunteer(108L));
        when(actionClient.getAction(209L)).thenReturn(action(209L));

        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 108,
                          "actionId": 209
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given().patch("/participations/{id}/cancel", id)
                .then()
                .statusCode(200);

        given().patch("/participations/{id}/cancel", id)
                .then()
                .statusCode(409);
    }

    @Test
    void actionUpdatedCreatesNotifications() {
        when(userClient.getUser(103L)).thenReturn(volunteer(103L));
        when(actionClient.getAction(203L)).thenReturn(action(203L));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 103,
                          "actionId": 203
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body("{\"actionId\": 203}")
                .post("/notifications/action-updated")
                .then()
                .statusCode(201);

        given().get("/volunteers/{id}/notifications", 103)
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }

    @Test
    void actionCancelledCreatesNotifications() {
        when(userClient.getUser(104L)).thenReturn(volunteer(104L));
        when(actionClient.getAction(204L)).thenReturn(action(204L));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 104,
                          "actionId": 204
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body("{\"actionId\": 204}")
                .post("/notifications/action-cancelled")
                .then()
                .statusCode(201);

        given().get("/volunteers/{id}/notifications", 104)
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }

    @Test
    void searchesParticipationsByVolunteerAndStatus() {
        when(userClient.getUser(105L)).thenReturn(volunteer(105L));
        when(actionClient.getAction(205L)).thenReturn(action(205L));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 105,
                          "actionId": 205
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(201);

        given().get("/participations?volunteerId=105&status=CONFIRMED")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
    }

    @Test
    void acceptedStatusSearchReturnsConfirmedParticipation() {
        when(userClient.getUser(109L)).thenReturn(volunteer(109L));
        when(actionClient.getAction(210L)).thenReturn(action(210L));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 109,
                          "actionId": 210
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(201);

        given().get("/participations?volunteerId=109&status=ACCEPTED")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].status", equalTo("CONFIRMED"));
    }

    @Test
    void returns400WhenActionNotificationHasNoActionId() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .post("/notifications/action-updated")
                .then()
                .statusCode(400);
    }

    @Test
    void returns503WhenUserServiceUnavailable() {
        when(userClient.getUser(110L)).thenThrow(new ProcessingException("down"));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 110,
                          "actionId": 211
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(503);
    }

    @Test
    void returns503WhenActionServiceUnavailable() {
        when(userClient.getUser(111L)).thenReturn(volunteer(111L));
        when(actionClient.getAction(212L)).thenThrow(new ProcessingException("down"));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "volunteerId": 111,
                          "actionId": 212
                        }
                        """)
                .post("/participations")
                .then()
                .statusCode(503);
    }

    private UserSummary volunteer(Long id) {
        UserSummary user = new UserSummary();
        user.id = id;
        user.type = "VOLUNTEER";
        user.username = "volunteer" + id;
        user.email = "volunteer" + id + "@example.com";
        return user;
    }

    private ActionSummary action(Long id) {
        ActionSummary action = new ActionSummary();
        action.id = id;
        action.type = "ACTIVISM";
        action.title = "Tree planting";
        action.startDate = LocalDateTime.of(2026, 7, 20, 9, 0);
        action.endDate = LocalDateTime.of(2026, 7, 20, 12, 0);
        return action;
    }
}
