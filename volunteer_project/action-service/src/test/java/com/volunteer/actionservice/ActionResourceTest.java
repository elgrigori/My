package com.volunteer.actionservice;

import com.volunteer.actionservice.adapters.out.ParticipationClient;
import com.volunteer.actionservice.adapters.out.UserClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class ActionResourceTest extends IntegrationBase {
    @InjectMock
    @RestClient
    UserClient userClient;

    @InjectMock
    @RestClient
    ParticipationClient participationClient;

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
    void createsContributeActionWithProducts() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "CONTRIBUTE",
                          "title": "Food collection",
                          "description": "Collect non-perishable food",
                          "startDate": "2026-09-01T09:00:00",
                          "endDate": "2026-09-01T18:00:00",
                          "location": "Patras",
                          "category": "food",
                          "products": [
                            { "name": "Rice", "targetQuantity": 100 },
                            { "name": "Pasta", "targetQuantity": 60 }
                          ]
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201)
                .body("type", equalTo("CONTRIBUTE"))
                .body("products", hasSize(2))
                .body("products[0].remainingQuantity", equalTo(100));
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
    void returns404WhenOrganizationMissing() {
        when(userClient.organizationExists(101L)).thenReturn(Response.status(Response.Status.NOT_FOUND).build());

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 101,
                          "title": "Missing org",
                          "description": "Test missing org",
                          "startAt": "2026-10-01T09:00:00",
                          "endAt": "2026-10-01T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 10
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(404);
    }

    @Test
    void searchesActionsByTypeStatusAndOrganization() {
        when(userClient.organizationExists(102L)).thenReturn(Response.ok().build());
        when(userClient.organizationExists(103L)).thenReturn(Response.ok().build());

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 102,
                          "title": "Cleanup one",
                          "description": "Test1",
                          "startAt": "2026-11-10T09:00:00",
                          "endAt": "2026-11-10T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 10
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201);

        int completedId = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 103,
                          "title": "Completed action",
                          "description": "Test2",
                          "startAt": "2026-11-15T09:00:00",
                          "endAt": "2026-11-15T13:00:00",
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

        given().patch("/actions/{id}/complete", completedId).then().statusCode(200);

        given()
                .queryParam("type", "ACTIVISM")
                .queryParam("status", "COMPLETED")
                .queryParam("organizationId", 103)
                .when()
                .get("/actions")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].title", equalTo("Completed action"));
    }

    @Test
    void updatesActionQuantitiesWithParticipationAcceptedAndCancelled() {
        when(userClient.organizationExists(110L)).thenReturn(Response.ok().build());

        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "FUNDING",
                          "organizationId": 110,
                          "title": "Fundraiser",
                          "description": "Raise funds",
                          "startAt": "2026-12-01T09:00:00",
                          "endAt": "2026-12-30T17:00:00",
                          "location": "Athens",
                          "targetAmount": 1000
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": 150}")
                .patch("/actions/{id}/participation-accepted", id)
                .then()
                .statusCode(200)
                .body("raisedAmount", equalTo(150.0f));

        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": 150}")
                .patch("/actions/{id}/participation-cancelled", id)
                .then()
                .statusCode(200)
                .body("raisedAmount", equalTo(0.0f));
    }

    @Test
    void listsActionsForOrganization() {
        when(userClient.organizationExists(120L)).thenReturn(Response.ok().build());
        when(userClient.organizationExists(130L)).thenReturn(Response.ok().build());

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 120,
                          "title": "Org cleanup",
                          "description": "Test group",
                          "startAt": "2027-01-01T09:00:00",
                          "endAt": "2027-01-01T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 10
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "type": "ACTIVISM",
                          "organizationId": 120,
                          "title": "Org cleanup 2",
                          "description": "Test group 2",
                          "startAt": "2027-01-03T09:00:00",
                          "endAt": "2027-01-03T13:00:00",
                          "location": "Athens",
                          "minParticipants": 2,
                          "totalParticipants": 10
                        }
                        """)
                .post("/actions")
                .then()
                .statusCode(201);

        given().get("/organizations/{id}/actions", 120)
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
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
