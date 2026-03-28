package com.qa.posts;

import com.qa.base.BaseTest;
import com.qa.dataprovider.PostDataProvider;
import com.qa.models.Post;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests for POST /posts on JSONPlaceholder.
 *
 * Uses a {@link PostDataProvider} to run the create test against
 * multiple different payloads in a data-driven fashion.
 */
@Feature("Posts API")
public class CreatePostTest extends BaseTest {

    /**
     * Verifies that POST /posts with a valid payload returns 201 Created
     * and that the response body echoes back the submitted fields.
     *
     * <p>JSONPlaceholder does not persist data, but it does return a
     * simulated response with an auto-generated {@code id}.
     *
     * @param title  post title from the data provider
     * @param body   post body from the data provider
     * @param userId user ID from the data provider
     */
    @Test(
        dataProvider = "validPosts",
        dataProviderClass = PostDataProvider.class,
        groups = {"smoke", "regression"}
    )
    @Description("POST /posts with valid payload should return 201 and echo the submitted fields")
    @Story("Create a post")
    @Severity(SeverityLevel.CRITICAL)
    public void createPostReturns201(String title, String body, int userId) {
        Post payload = Post.builder()
                .title(title)
                .body(body)
                .userId(userId)
                .build();

        postsClient.create(payload)
                .statusCode(201)
                .body("id",     notNullValue())
                .body("title",  equalTo(title))
                .body("body",   equalTo(body))
                .body("userId", equalTo(userId));
    }
}
