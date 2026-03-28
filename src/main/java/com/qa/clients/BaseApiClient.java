package com.qa.clients;

import com.qa.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for all API clients.
 * Builds a shared {@link RequestSpecification} with:
 * <ul>
 *   <li>Base URI from constructor argument</li>
 *   <li>Content-Type: application/json</li>
 *   <li>Relaxed HTTPS validation (no cert errors in test environments)</li>
 *   <li>Allure RestAssured filter for request/response logging in reports</li>
 *   <li>Optional request/response logging based on config properties</li>
 * </ul>
 */
public abstract class BaseApiClient {

    private static final Logger log = LogManager.getLogger(BaseApiClient.class);

    /** Shared RequestSpecification used by all methods in subclasses. */
    protected final RequestSpecification spec;

    /**
     * Constructs the client and builds the {@link RequestSpecification}.
     *
     * @param baseUrl the base URI for all requests made by this client
     */
    public BaseApiClient(String baseUrl) {
        log.debug("Initialising API client with baseUrl: {}", baseUrl);

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new AllureRestAssured()
                        .setRequestTemplate("http-request.ftl")
                        .setResponseTemplate("http-response.ftl"));

        // Conditional logging based on environment config
        boolean logRequest  = ConfigManager.getBoolean("log.request");
        boolean logResponse = ConfigManager.getBoolean("log.response");

        if (logRequest) {
            builder.log(LogDetail.ALL);
        }

        // Store the built spec
        this.spec = builder.build();

        log.debug("RequestSpecification built. logRequest={}, logResponse={}", logRequest, logResponse);
    }
}
