package com.qa.dataprovider;

import org.testng.annotations.DataProvider;

/**
 * TestNG DataProvider supplying valid post payloads for data-driven tests.
 *
 * <p>Each row provides: {title, body, userId}
 *
 * <p>Usage in test classes:
 * <pre>
 *   {@literal @}Test(dataProvider = "validPosts", dataProviderClass = PostDataProvider.class)
 *   public void createPost(String title, String body, int userId) { ... }
 * </pre>
 */
public class PostDataProvider {

    private PostDataProvider() {
        // Utility class
    }

    /**
     * Returns an array of valid post payloads.
     *
     * <p>Columns: [title (String), body (String), userId (int)]
     *
     * @return Object[][] with 3 rows of test data
     */
    @DataProvider(name = "validPosts", parallel = false)
    public static Object[][] validPosts() {
        return new Object[][] {
            {
                "Introduction to RestAssured Testing",
                "RestAssured provides a Java DSL for simplifying testing of REST based services. " +
                "It supports XML and JSON request and response validation.",
                1
            },
            {
                "TestNG Data-Driven Approach",
                "Data providers allow you to pass different data sets to the same test method, " +
                "reducing code duplication and improving test coverage.",
                2
            },
            {
                "Allure Reporting Integration",
                "Allure framework generates clear and beautiful reports that show test execution history, " +
                "steps, attachments, timing, and severity classification.",
                3
            }
        };
    }
}
