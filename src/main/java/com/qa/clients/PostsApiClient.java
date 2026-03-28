package com.qa.clients;

import com.qa.models.Post;
import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * API client for the /posts resource on JSONPlaceholder.
 *
 * Supported operations:
 * <ul>
 *   <li>GET /posts           — {@link #getAll()}</li>
 *   <li>GET /posts/{id}      — {@link #getById(int)}</li>
 *   <li>GET /posts?userId=n  — {@link #getByUserId(int)}</li>
 *   <li>POST /posts          — {@link #create(Post)}</li>
 *   <li>PUT /posts/{id}      — {@link #update(int, Post)}</li>
 *   <li>PATCH /posts/{id}    — {@link #patch(int, Map)}</li>
 *   <li>DELETE /posts/{id}   — {@link #delete(int)}</li>
 * </ul>
 */
public class PostsApiClient extends BaseApiClient {

    private static final Logger log = LogManager.getLogger(PostsApiClient.class);
    private static final String POSTS_PATH = "/posts";

    /**
     * Creates a PostsApiClient targeting the given base URL.
     *
     * @param baseUrl the base URL, e.g. "https://jsonplaceholder.typicode.com"
     */
    public PostsApiClient(String baseUrl) {
        super(baseUrl);
    }

    /**
     * GET /posts — retrieves all posts.
     *
     * @return {@link ValidatableResponse} for fluent assertion chaining
     */
    public ValidatableResponse getAll() {
        log.info("GET {}", POSTS_PATH);
        return given(spec)
                .when()
                .get(POSTS_PATH)
                .then();
    }

    /**
     * GET /posts/{id} — retrieves a single post by ID.
     *
     * @param id the post identifier
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse getById(int id) {
        log.info("GET {}/{}", POSTS_PATH, id);
        return given(spec)
                .when()
                .get(POSTS_PATH + "/{id}", id)
                .then();
    }

    /**
     * GET /posts?userId={userId} — retrieves posts filtered by user ID.
     *
     * @param userId the user identifier to filter by
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse getByUserId(int userId) {
        log.info("GET {}?userId={}", POSTS_PATH, userId);
        return given(spec)
                .queryParam("userId", userId)
                .when()
                .get(POSTS_PATH)
                .then();
    }

    /**
     * POST /posts — creates a new post.
     *
     * @param post the post payload to send as JSON body
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse create(Post post) {
        log.info("POST {} with title='{}'", POSTS_PATH, post.getTitle());
        return given(spec)
                .body(post)
                .when()
                .post(POSTS_PATH)
                .then();
    }

    /**
     * PUT /posts/{id} — fully replaces an existing post.
     *
     * @param id   the post identifier
     * @param post the replacement post payload
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse update(int id, Post post) {
        log.info("PUT {}/{}", POSTS_PATH, id);
        return given(spec)
                .body(post)
                .when()
                .put(POSTS_PATH + "/{id}", id)
                .then();
    }

    /**
     * PATCH /posts/{id} — partially updates an existing post.
     *
     * @param id     the post identifier
     * @param fields a map of field names to new values
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse patch(int id, Map<String, Object> fields) {
        log.info("PATCH {}/{} with fields: {}", POSTS_PATH, id, fields.keySet());
        return given(spec)
                .body(fields)
                .when()
                .patch(POSTS_PATH + "/{id}", id)
                .then();
    }

    /**
     * DELETE /posts/{id} — deletes a post.
     *
     * @param id the post identifier
     * @return {@link ValidatableResponse}
     */
    public ValidatableResponse delete(int id) {
        log.info("DELETE {}/{}", POSTS_PATH, id);
        return given(spec)
                .when()
                .delete(POSTS_PATH + "/{id}", id)
                .then();
    }
}
