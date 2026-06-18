package com.volunteer.participationservice;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class RootResourceTest {

    @Test
    void returnsParticipationServiceHomePage() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(containsString("Participation Service"))
                .body(containsString("RUNNING"))
                .body(containsString("/participations"));
    }
}
