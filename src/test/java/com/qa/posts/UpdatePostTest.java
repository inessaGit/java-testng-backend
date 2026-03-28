package com.qa.posts;

import com.qa.base.BaseTest;
import com.qa.models.Post;
import com.qa.utils.DataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests for PUT /posts/{id} and PATCH /posts/{id} on JSONPlaceholder.
 *
 * JSONPlaceholder simulates updates — responses echo the submitted data
 * but nothing is actually persisted server-side.
 */
@Feature("Posts API")
public class UpdatePostTest extends BaseTest {

    private static final int TARGET_POST_ID = 1;

    /**
     * Verifies that PUT /posts/1 with a full replacement payload returns
     * HTTP 200 and reflects all updated fields in the response.
     */
    @Test(groups = {"regression"})
    @Description("PUT /posts/{id} should return 200 and reflect all updated fields")
    @Story("Update a post")
    @Severity(SeverityLevel.CRITICAL)
    public void putPostReturns200WithUpdatedFields() {
        String newTitle = "Updated Title " + DataFactory.randomString(8);
        String newBody  = "Updated body content " + DataFactory.randomString(20);
        int    newUser  = 3;

        Post updatedPost = Post.builder()
                .id(TARGET_POST_ID)
                .userId(newUser)
                .title(newTitle)
                .body(newBody)
                .build();

        postsClient.update(TARGET_POST_ID, updatedPost)
                .statusCode(200)
                .body("id",     equalTo(TARGET_POST_ID))
                .body("userId", equalTo(newUser))
                .body("title",  equalTo(newTitle))
                .body("body",   equalTo(newBody));
    }

    /**
     * Verifies that PATCH /posts/1 with a partial update payload returns
     * HTTP 200 and reflects only the patched field.
     */
    @Test(groups = {"regression"})
    @Description("PATCH /posts/{id} should return 200 and reflect the patched fields")
    @Story("Partially update a post")
    @Severity(SeverityLevel.NORMAL)
    public void patchPostReturns200WithPatchedField() {
        String patchedTitle = "Patched Title " + DataFactory.randomString(6);

        Map<String, Object> patch = Map.of("title", patchedTitle);

        postsClient.patch(TARGET_POST_ID, patch)
                .statusCode(200)
                .body("id",    notNullValue())
                .body("title", equalTo(patchedTitle));
    }
}
