package com.qa.base;

import com.qa.clients.PostsApiClient;
import com.qa.clients.UsersApiClient;
import com.qa.config.ConfigManager;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

/**
 * Base class for all test classes.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Attaches the Allure TestNG listener for report generation</li>
 *   <li>Initialises shared API client instances before the test suite</li>
 *   <li>Resets RestAssured global state after the suite completes</li>
 * </ul>
 *
 * <p>Test classes extend this class and use the protected client fields directly:
 * <pre>
 *   public class GetPostsTest extends BaseTest {
 *       {@literal @}Test
 *       public void getAllReturns200() {
 *           postsClient.getAll().statusCode(200);
 *       }
 *   }
 * </pre>
 */
@Listeners(AllureTestNg.class)
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    /** Client for /posts operations on JSONPlaceholder. */
    protected static PostsApiClient postsClient;

    /** Client for /login and /register operations on ReqRes. */
    protected static UsersApiClient usersClient;

    /**
     * Runs once before the entire test suite.
     * Enables global RestAssured validation-failure logging and
     * creates the shared API client instances.
     */
    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        log.info("=== Test Suite Starting ===");
        log.info("Environment: {}", System.getProperty("env", "dev"));

        // Enable extra logging when a response validation fails
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        String jsonplaceholderBaseUrl = ConfigManager.get("jsonplaceholder.base.url");
        String reqresBaseUrl          = ConfigManager.get("reqres.base.url");

        log.info("JSONPlaceholder base URL : {}", jsonplaceholderBaseUrl);
        log.info("ReqRes base URL          : {}", reqresBaseUrl);

        String reqresApiKey = ConfigManager.get("reqres.api.key", "");

        postsClient = new PostsApiClient(jsonplaceholderBaseUrl);
        usersClient = new UsersApiClient(reqresBaseUrl, reqresApiKey);

        log.info("API clients initialised successfully.");
    }

    /**
     * Runs once after the entire test suite completes.
     * Resets RestAssured to its default global state.
     */
    @AfterSuite(alwaysRun = true)
    public void globalTeardown() {
        log.info("=== Test Suite Finished ===");
        RestAssured.reset();
    }
}
