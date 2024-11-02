package trello.api.exam;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseTestSetup;
import io.restassured.response.Response;

import static com.telerikacademy.api.tests.Constants.LIST_NAME;
import static com.telerikacademy.api.tests.Endpoints.BOARD_LISTS_ENDPOINT;
import static io.restassured.RestAssured.basePath;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class ListTest extends BaseTestSetup {
    @BeforeClass
    public void listTestSetup() {
        if (isNull(boardId)) {
            BoardTest boardTest = new BoardTest();
            boardTest.createBoardTest();
        }
    }

    @Test
    public void createListTest() {
        basePath = BOARD_LISTS_ENDPOINT;

        String listNameUnique = format("%s %s", LIST_NAME, uniqueName);

        Response response = getApplicationAuthentication()
            .pathParam("id", boardId)
            .pathParam("lists", "lists")
            .queryParam("name", listNameUnique)
            .when()
            .post();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("name"), listNameUnique, "List names don't match.");
        assertEquals(response.getBody().jsonPath().getString("idBoard"), boardId, "Board ids don't match.");

        toDoListId = response.getBody().jsonPath().getString("id");

        System.out.printf("List with name '%s' and id '%s' was successfully created.%n", listNameUnique, toDoListId);
    }
}
