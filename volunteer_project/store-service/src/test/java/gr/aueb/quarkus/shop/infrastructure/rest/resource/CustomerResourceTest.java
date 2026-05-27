package gr.aueb.quarkus.shop.infrastructure.rest.resource;

import gr.aueb.quarkus.shop.common.JPATest;
import gr.aueb.quarkus.shop.infrastructure.rest.ApiPath;
import gr.aueb.quarkus.shop.infrastructure.rest.representation.CartRepresentation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CustomerResourceTest extends JPATest {

    @Test
    void getCurrentCart() {

        CartRepresentation cartRepresentation = given()
                .when().get(ApiPath.Root.CUSTOMERS + "/101/cart")
                .then()
                .statusCode(200)
                .extract()
                .as(CartRepresentation.class);

        assertEquals(1, cartRepresentation.cartItems.size());
        assertEquals(101, cartRepresentation.customer.id);

    }
}