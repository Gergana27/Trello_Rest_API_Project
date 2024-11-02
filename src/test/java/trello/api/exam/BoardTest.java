package trello.api.exam;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.Test;

import base.BaseTestSetup;
import io.restassured.response.Response;

import static com.telerikacademy.api.tests.Constants.BOARD_NAME;
import static com.telerikacademy.api.tests.Endpoints.BOARDS_ENDPOINT;
import static com.telerikacademy.api.tests.Endpoints.BOARD_ENDPOINT;
import static io.restassured.RestAssured.basePath;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class BoardTest extends BaseTestSetup {

    @Test
    public void createBoardTest() {
        basePath = BOARDS_ENDPOINT;

        String boardNameUnique = format("%s %s", BOARD_NAME, timestamp);

        Response response = getApplicationAuthentication()
            .queryParam("name", boardNameUnique)
            .when()
            .post();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("name"), boardNameUnique, "Board names don't match.");

        boardId = response.getBody().jsonPath().getString("id");

        System.out.printf("Board with name '%s' and id '%s' was successfully created.%n", boardNameUnique, boardId);
    }

    @Test
    public void getBoardListsTest() {

        if (isNull(boardId)) {
            createBoardTest();
        }

        basePath = BOARD_ENDPOINT;

        Response response = getApplicationAuthentication()
            .pathParam("id", boardId)
            .queryParam("lists", "all")
            .when()
            .get();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));

        ArrayList<HashMap<String, String>> lists = response.getBody().jsonPath().get("lists");
        for (HashMap<String, String> list : lists) {
            if (list.get("name").equals("To Do")) {
                toDoListId = list.get("id");
                break;
            }
        }

        System.out.printf("List 'To Do' id is: %s.%n", toDoListId);
    }
}
