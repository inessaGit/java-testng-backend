package com.qa.posts;

import com.qa.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anEmptyMap;

/**
 * Tests for DELETE /posts/{id} on JSONPlaceholder.
 *
 * JSONPlaceholder simulates the delete — it returns 200 with an empty body
 * but does not actually remove the resource.
 */
@Feature("Posts API")
public class DeletePostTest extends BaseTest {

    private static final int TARGET_POST_ID = 1;

    /**
     * Verifies that DELETE /posts/1 returns HTTP 200.
     * The response body should be an empty JSON object {}.
     */
    @Test(groups = {"smoke", "regression"})
    @Description("DELETE /posts/{id} should return HTTP 200 and an empty response body")
    @Story("Delete a post")
    @Severity(SeverityLevel.CRITICAL)
    public void deletePostReturns200() {
        postsClient.delete(TARGET_POST_ID)
                .statusCode(200)
                .body("$", anEmptyMap());
    }
}
