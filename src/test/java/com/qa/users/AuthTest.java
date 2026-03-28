package com.qa.users;

import com.qa.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests for POST /login and POST /register on ReqRes.
 *
 * ReqRes only accepts a fixed set of predefined email addresses.
 * Valid credentials: eve.holt@reqres.in / cityslicka  (login)
 *                    eve.holt@reqres.in / pistol       (register)
 */
@Feature("Auth API")
public class AuthTest extends BaseTest {

    // ReqRes predefined valid credentials
    private static final String VALID_EMAIL    = "eve.holt@reqres.in";
    private static final String VALID_PASSWORD = "cityslicka";
    private static final String REGISTER_PASS  = "pistol";

    // An email not in the ReqRes user list
    private static final String UNKNOWN_EMAIL  = "unknown.user@notinreqres.com";

    /**
     * POST /login with valid credentials should return 200 and a token.
     */
    @Test(groups = {"smoke", "regression"})
    @Description("POST /login with valid credentials should return 200 and a non-null token")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    public void loginSuccess200() {
        usersClient.login(VALID_EMAIL, VALID_PASSWORD)
                .statusCode(200)
                .body("token", notNullValue());
    }

    /**
     * POST /login without a password should return 400 with an error message.
     * The ReqRes API returns: {"error": "Missing password"}
     */
    @Test(groups = {"regression"})
    @Description("POST /login without password should return 400 with 'Missing password' error")
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    public void loginMissingPassword400() {
        usersClient.login(VALID_EMAIL, null)
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    /**
     * POST /register with a predefined ReqRes email and password should return
     * 200 with an id and token in the response body.
     */
    @Test(groups = {"smoke", "regression"})
    @Description("POST /register with valid credentials should return 200 with id and token")
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    public void registerSuccess200() {
        usersClient.register(VALID_EMAIL, REGISTER_PASS)
                .statusCode(200)
                .body("id",    notNullValue())
                .body("token", notNullValue());
    }

    /**
     * POST /register with an undefined (non-ReqRes) user email should return
     * 400 with an error message indicating the user is not defined.
     * The ReqRes API returns: {"error": "Note: Only defined users succeed registration"}
     */
    @Test(groups = {"regression"})
    @Description("POST /register with unknown email should return 400 with undefined user error")
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    public void registerUndefinedUser400() {
        usersClient.register(UNKNOWN_EMAIL, "anypassword")
                .statusCode(400)
                .body("error", notNullValue());
    }
}
