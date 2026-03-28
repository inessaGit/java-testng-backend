package com.qa.posts;

import com.qa.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests for GET /posts and GET /posts/{id} endpoints on JSONPlaceholder.
 *
 * Groups:
 *   smoke      — fast health-check tests run on every build
 *   regression — full coverage tests
 */
@Feature("Posts API")
public class GetPostsTest extends BaseTest {

    /**
     * Verifies that GET /posts returns HTTP 200.
     * Belongs to both smoke and regression suites.
     */
    @Test(groups = {"smoke", "regression"})
    @Description("GET /posts should return HTTP 200 OK")
    @Story("Retrieve all posts")
    @Severity(SeverityLevel.BLOCKER)
    public void getAllReturns200() {
        postsClient.getAll()
                .statusCode(200);
    }

    /**
     * Verifies that GET /posts returns exactly 100 items.
     * JSONPlaceholder always has 100 seeded posts.
     */
    @Test(groups = {"regression"})
    @Description("GET /posts should return exactly 100 posts")
    @Story("Retrieve all posts")
    @Severity(SeverityLevel.NORMAL)
    public void getAllHas100Items() {
        postsClient.getAll()
                .statusCode(200)
                .body("size()", equalTo(100))
                .body("[0].userId", notNullValue())
                .body("[0].id",     notNullValue())
                .body("[0].title",  notNullValue())
                .body("[0].body",   notNullValue());
    }

    /**
     * Verifies that GET /posts/1 response body matches the JSON Schema
     * defined in schemas/post-schema.json.
     */
    @Test(groups = {"regression"})
    @Description("GET /posts/{id} response body should match the Post JSON Schema")
    @Story("Retrieve single post")
    @Severity(SeverityLevel.CRITICAL)
    public void getSingleMatchesSchema() {
        postsClient.getById(1)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"))
                .body("id",     equalTo(1))
                .body("userId", greaterThan(0))
                .body("title",  notNullValue())
                .body("body",   notNullValue());
    }

    /**
     * Verifies that requesting a non-existent post returns HTTP 404.
     */
    @Test(groups = {"smoke", "regression"})
    @Description("GET /posts/{id} with an unknown ID should return HTTP 404")
    @Story("Retrieve single post")
    @Severity(SeverityLevel.NORMAL)
    public void getNonExistentReturns404() {
        postsClient.getById(99999)
                .statusCode(404);
    }
}
