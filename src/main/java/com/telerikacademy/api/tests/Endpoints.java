package com.telerikacademy.api.tests;

import static java.lang.String.format;

public class Endpoints {

    public static final String BASE_URL = "https://api.trello.com";
    public static final String API_VERSION = "/1";

    public static final String AUTH_ENDPOINT = format("%s%s", API_VERSION, "/members/me");

    public static final String BOARDS_ENDPOINT = format("%s%s", API_VERSION, "/boards");
    public static final String BOARD_ENDPOINT = format("%s%s", API_VERSION, "/boards/{id}");
    public static final String BOARD_LISTS_ENDPOINT = format("%s%s", API_VERSION, "/boards/{id}/{lists}");

    public static final String CARDS_ENDPOINT = format("%s%s", API_VERSION, "/cards");
    public static final String CARD_ENDPOINT = format("%s%s", API_VERSION, "/cards/{id}");

}
