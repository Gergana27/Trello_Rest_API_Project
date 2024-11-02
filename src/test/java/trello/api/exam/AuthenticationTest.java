package trello.api.exam;

import org.testng.annotations.Test;

import base.BaseTestSetup;
import io.restassured.response.Response;

import static com.telerikacademy.api.tests.Constants.FULL_NAME;
import static com.telerikacademy.api.tests.Endpoints.AUTH_ENDPOINT;
import static io.restassured.RestAssured.basePath;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class AuthenticationTest extends BaseTestSetup {

    @Test
    public void authenticationTest() {

        basePath = AUTH_ENDPOINT;

        Response response = getApplicationAuthentication()
            .when()
            .get();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("fullName"), FULL_NAME, "Full names don't match.");

        System.out.println("Authentication works!");
    }
}
