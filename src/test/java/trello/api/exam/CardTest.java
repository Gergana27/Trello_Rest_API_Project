package trello.api.exam;

import java.time.Instant;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseTestSetup;
import io.restassured.response.Response;

import static com.telerikacademy.api.tests.Constants.CARD_DESCRIPTION;
import static com.telerikacademy.api.tests.Constants.CARD_NAME;
import static com.telerikacademy.api.tests.Constants.NEW_CARD_DESCRIPTION;
import static com.telerikacademy.api.tests.Constants.NEW_CARD_NAME;
import static com.telerikacademy.api.tests.Endpoints.CARDS_ENDPOINT;
import static com.telerikacademy.api.tests.Endpoints.CARD_ENDPOINT;
import static io.restassured.RestAssured.basePath;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CardTest extends BaseTestSetup {

    @BeforeClass
    public void cardTestSetup() {
        if (isNull(boardId)) {
            BoardTest boardTest = new BoardTest();
            boardTest.createBoardTest();
        }

        if (isNull(toDoListId)) {
            ListTest listTest = new ListTest();
            listTest.createListTest();
        }
    }

    @Test
    public void createCardTest() {

        basePath = CARDS_ENDPOINT;

        String cardNameUnique = format("%s %s", CARD_NAME, uniqueName);
        String carDescriptionUnique = format("%s %s", CARD_DESCRIPTION, uniqueName);

        Response response = getApplicationAuthentication()
            .queryParam("idList", toDoListId)
            .queryParam("name", cardNameUnique)
            .queryParam("desc", carDescriptionUnique)
            .when()
            .post();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("name"), cardNameUnique, "Card names don't match.");
        assertEquals(response.getBody().jsonPath().getString("desc"), carDescriptionUnique, "Card description don't match.");
        assertEquals(response.getBody().jsonPath().getString("idList"), toDoListId, "List ids don't match.");

        String dateLastActivity = response.getBody().jsonPath().getString("dateLastActivity");
        Instant actualDate = Instant.parse(dateLastActivity);
        Instant expectedDate = Instant.parse(timestamp);
        assertTrue((actualDate.isAfter(expectedDate)), "List ids don't match.");

        cardId = response.getBody().jsonPath().getString("id");

        System.out.printf("Card with name '%s' and id '%s' was successfully created.%n", cardNameUnique, cardId);
    }

    @Test
    public void updateCardNameAndDescriptionQueryParamTest() {

        if (isNull(cardId)) {
            createCardTest();
        }

        basePath = CARD_ENDPOINT;

        Response response = getApplicationAuthentication()
            .pathParam("id", cardId)
            .queryParam("name", NEW_CARD_NAME)
            .queryParam("desc", NEW_CARD_DESCRIPTION)
            .when()
            .put();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("name"), NEW_CARD_NAME, "Card names don't match.");
        assertEquals(response.getBody().jsonPath().getString("desc"), NEW_CARD_DESCRIPTION, "Card description don't match.");
        assertEquals(response.getBody().jsonPath().getString("id"), cardId, "Card ids don't match.");

        System.out.println("Card name and description was successfully updated.");
    }

    @Test
    public void updateCardCoverColorTest() {

        if (isNull(cardId)) {
            createCardTest();
        }

        basePath = CARD_ENDPOINT;
        String color = "lime";

        String coverColor = format("{\n" +
            "    \"cover\": {\n" +
            "        \"color\": \"%s\"\n" +
            "    }\n" +
            "}", color);

        Response response = getApplicationAuthentication()
            .pathParam("id", cardId)
            .contentType("application/json")
            .body(coverColor)
            .when()
            .put();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));

        Map<String, String> cover = response.getBody().jsonPath().get("cover");
        String actualColor = cover.get("color");
        assertEquals(actualColor, color, "Card cover colors don't match.");

        System.out.printf("Card cover color was successfully changed to '%s'.\n", color);
    }

    @Test
    public void moveCardToInTestListTest() {

        if (isNull(cardId)) {
            createCardTest();
        }

        basePath = CARD_ENDPOINT;

        Response response = getApplicationAuthentication()
            .pathParam("id", cardId)
            .queryParam("idList", toDoListId)
            .when()
            .put();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("idList"), toDoListId, "Card list names don't match.");

        System.out.println("Card was successfully move to 'In Test' list.");
    }
}
