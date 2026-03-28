package com.qa.clients;

import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * API client for authentication endpoints on ReqRes.
 *
 * Supported operations:
 * <ul>
 *   <li>POST /login    — {@link #login(String, String)}</li>
 *   <li>POST /register — {@link #register(String, String)}</li>
 * </ul>
 *
 * Base URL should be "https://reqres.in/api".
 */
public class UsersApiClient extends BaseApiClient {

    private static final Logger log = LogManager.getLogger(UsersApiClient.class);

    private static final String LOGIN_PATH    = "/login";
    private static final String REGISTER_PATH = "/register";

    private final String apiKey;

    /**
     * Creates a UsersApiClient targeting the given base URL.
     *
     * @param baseUrl the base URL, e.g. "https://reqres.in/api"
     * @param apiKey  the x-api-key value for ReqRes authentication (may be empty)
     */
    public UsersApiClient(String baseUrl, String apiKey) {
        super(baseUrl);
        this.apiKey = apiKey != null ? apiKey : "";
    }

    /**
     * POST /login — authenticates with email and password.
     *
     * Sends a JSON body: {"email": "...", "password": "..."}
     * On success (200) the response contains a "token" field.
     * On missing password the API returns 400 with an "error" field.
     *
     * @param email    the user's email address
     * @param password the user's password (may be null to trigger a 400)
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse login(String email, String password) {
        log.info("POST {} for email='{}'", LOGIN_PATH, email);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        if (password != null) {
            body.put("password", password);
        }

        return given(spec)
                .header("x-api-key", apiKey)
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    /**
     * POST /register — registers a new user with email and password.
     *
     * Sends a JSON body: {"email": "...", "password": "..."}
     * On success (200) the response contains "id" and "token".
     * If the email is not in the ReqRes predefined list, returns 400 with "error".
     *
     * @param email    the user's email address
     * @param password the user's password (may be null to trigger a 400)
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse register(String email, String password) {
        log.info("POST {} for email='{}'", REGISTER_PATH, email);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        if (password != null) {
            body.put("password", password);
        }

        return given(spec)
                .header("x-api-key", apiKey)
                .body(body)
                .when()
                .post(REGISTER_PATH)
                .then();
    }
}
