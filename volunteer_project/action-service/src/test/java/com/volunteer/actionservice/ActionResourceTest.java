package com.volunteer.actionservice;

import com.volunteer.actionservice.client.ParticipationClient;
import com.volunteer.actionservice.client.UserClient;
import com.volunteer.actionservice.repository.ActionRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class ActionResourceTest {
    @Inject
    ActionRepository actionRepository;

    @InjectMock
    @RestClient
    UserClient userClient;

    @InjectMock
    @RestClient
    ParticipationClient participationClient;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        actionRepository.deleteAll();
    }

    @Test
    void createsAndSearchesFundingAction() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "FUNDING",
                          "title": "School equipment fund",
                          "description": "Raise money for laptops",
                          "startDate": "2026-08-10T09:00:00",
                          "endDate": "2026-08-20T17:00:00",
                          "location": "Thessaloniki",
                          "category": "education",
                          "targetAmount": 5000
                        }
                        """)
                .when()
                .post("/actions")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("type", equalTo("FUNDING"));

        given()
                .queryParam("category", "education")
                .when()
                .get("/actions")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].title", equalTo("School equipment fund"));
    }

    @Test
    void deletesAction() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "DONATION",
                          "title": "Food collection",
                          "description": "Collect non-perishable food",
                          "startDate": "2026-09-01T09:00:00",
                          "endDate": "2026-09-01T18:00:00",
                          "location": "Patras",
                          "category": "donation",
                          "requiredItems": "Rice, pasta, cans"
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given().delete("/actions/{id}", id).then().statusCode(204);
        given().get("/actions/{id}", id).then().statusCode(404);
    }

    @Test
    void createsActionForOrganizationAndCompletesIt() {
        when(userClient.organizationExists(77L)).thenReturn(Response.ok().build());

        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 77,
                          "title": "Beach cleanup",
                          "description": "Clean the coast",
                          "startAt": "2026-10-01T09:00:00",
                          "endAt": "2026-10-01T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 20
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201)
                .body("organizationId", equalTo(77))
                .body("status", equalTo("OPEN"))
                .extract()
                .path("id");

        given().get("/actions/{id}/availability", id)
                .then()
                .statusCode(200)
                .body("available", equalTo(true));

        given().patch("/actions/{id}/complete", id)
                .then()
                .statusCode(200)
                .body("status", equalTo("COMPLETED"));
    }

    @Test
    void returns404ForMissingAction() {
        given().get("/actions/{id}", 9999).then().statusCode(404);
    }

    @Test
    void cancelsActionAndNotifiesParticipants() {
        when(userClient.organizationExists(88L)).thenReturn(Response.ok().build());

        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 88,
                          "title": "Cancel me",
                          "description": "Test cancel",
                          "startAt": "2026-11-01T09:00:00",
                          "endAt": "2026-11-01T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 10
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given().patch("/actions/{id}/cancel", id)
                .then()
                .statusCode(200)
                .body("status", equalTo("CANCELLED"));
    }

    @Test
    void returns409OnCompleteOfCancelledAction() {
        when(userClient.organizationExists(99L)).thenReturn(Response.ok().build());

        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 99,
                          "title": "Already cancelled",
                          "description": "Test",
                          "startAt": "2026-12-01T09:00:00",
                          "endAt": "2026-12-01T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 10
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given().patch("/actions/{id}/cancel", id).then().statusCode(200);
        given().patch("/actions/{id}/complete", id).then().statusCode(409);
    }
}
