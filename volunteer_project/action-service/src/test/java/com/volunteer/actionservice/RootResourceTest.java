package com.volunteer.actionservice;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class RootResourceTest {

    @Test
    void returnsActionServiceHomePage() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(containsString("Action Service"))
                .body(containsString("RUNNING"))
                .body(containsString("/actions"));
    }
}
