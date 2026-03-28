# ☕ Backend API Testing — Java + TestNG + RestAssured

> Backend integration test suite using **Java 17 + TestNG + RestAssured** targeting the [JSONPlaceholder](https://jsonplaceholder.typicode.com) and [ReqRes](https://reqres.in) public APIs. Demonstrates layered architecture, data-driven testing, parallel execution, and Maven-based CI reporting.

---

## 📋 Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Build](#installation--build)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Architecture](#test-architecture)
- [Test Suites](#test-suites)
- [Reporting](#reporting)
- [CI/CD Integration](#cicd-integration)

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17 | Language |
| TestNG | 7.8 | Test runner, parallel execution |
| RestAssured | 5.4 | HTTP DSL for API testing |
| Hamcrest | 2.2 | Fluent assertions |
| Allure TestNG | 2.24 | HTML test reports |
| Jackson | 2.16 | JSON (de)serialization / POJOs |
| Maven | 3.9 | Build & dependency management |
| Lombok | 1.18 | Boilerplate reduction |
| Log4j2 | 2.22 | Structured logging |

---

## Project Structure

```
java-testng-backend/
├── src/
│   ├── main/java/com/qa/
│   │   ├── config/
│   │   │   └── ConfigManager.java       # Reads .properties files
│   │   ├── clients/
│   │   │   ├── BaseApiClient.java       # RequestSpecification builder
│   │   │   ├── PostsApiClient.java      # /posts CRUD methods
│   │   │   └── UsersApiClient.java      # /users + auth methods
│   │   ├── models/
│   │   │   ├── Post.java                # Jackson POJO
│   │   │   └── AuthRequest.java
│   │   └── utils/
│   │       └── DataFactory.java         # Random test data builders
│   └── test/java/com/qa/
│       ├── base/
│       │   └── BaseTest.java            # @BeforeSuite setup
│       ├── posts/
│       │   ├── GetPostsTest.java
│       │   ├── CreatePostTest.java
│       │   ├── UpdatePostTest.java
│       │   └── DeletePostTest.java
│       ├── users/
│       │   └── AuthTest.java
│       └── dataprovider/
│           └── PostDataProvider.java    # TestNG @DataProvider
├── src/test/resources/
│   ├── schemas/
│   │   └── post-schema.json             # JSON Schema files
│   └── testng/
│       ├── smoke-suite.xml
│       ├── regression-suite.xml
│       └── parallel-suite.xml
├── config/
│   ├── dev.properties
│   └── ci.properties
├── pom.xml
└── README.md
```

---

## Prerequisites

- **Java 17+** — verify with `java -version`
- **Maven 3.9+** — verify with `mvn -version`

---

## Installation & Build

```bash
# 1. Clone the repository
git clone https://github.com/inessaGit/java-testng-backend.git
cd java-testng-backend

# 2. Compile and download dependencies
mvn clean compile

# 3. (Optional) Verify setup without running tests
mvn validate
```

---

## Configuration

```properties
# config/dev.properties
jsonplaceholder.base.url=https://jsonplaceholder.typicode.com
reqres.base.url=https://reqres.in/api
request.timeout=10000
log.request=true
log.response=true
```

Pass environment at runtime:
```bash
mvn test -Denv=ci
```

---

## Running Tests

```bash
# Run all tests (default suite)
mvn clean test

# Run smoke suite only
mvn test -DsuiteFile=src/test/resources/testng/smoke-suite.xml

# Run in parallel (thread-count=4)
mvn test -DsuiteFile=src/test/resources/testng/parallel-suite.xml

# Run with specific tag/group
mvn test -Dgroups=smoke

# Run and generate Allure report
mvn clean test
mvn allure:serve
```

---

## Test Architecture

### BaseTest.java

```java
@Listeners(AllureTestNg.class)
public class BaseTest {

    protected PostsApiClient postsClient;
    protected UsersApiClient usersClient;

    @BeforeSuite
    public void globalSetup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        String baseUrl = ConfigManager.get("jsonplaceholder.base.url");
        postsClient = new PostsApiClient(baseUrl);
        usersClient = new UsersApiClient(ConfigManager.get("reqres.base.url"));
    }

    @AfterSuite
    public void globalTeardown() {
        RestAssured.reset();
    }
}
```

### BaseApiClient.java

```java
public abstract class BaseApiClient {

    protected final RequestSpecification spec;

    public BaseApiClient(String baseUrl) {
        spec = new RequestSpecBuilder()
            .setBaseUri(baseUrl)
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .addFilter(new AllureRestAssured())
            .build();
    }
}
```

---

## Test Suites

### GET /posts — `GetPostsTest.java`

```java
public class GetPostsTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Description("GET /posts returns 200 with 100 items")
    public void getAllPostsReturns200() {
        postsClient.getAll()
            .then()
            .statusCode(200)
            .body("size()", equalTo(100))
            .body("[0].userId", notNullValue());
    }

    @Test(groups = {"regression"})
    @Description("GET /posts/1 returns correct post schema")
    public void getSinglePostMatchesSchema() {
        postsClient.getById(1)
            .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(groups = {"regression"})
    @Description("GET /posts/9999 returns 404")
    public void getNonExistentPostReturns404() {
        postsClient.getById(9999)
            .then()
            .statusCode(404);
    }
}
```

### POST /posts with DataProvider

```java
public class CreatePostTest extends BaseTest {

    @Test(dataProvider = "validPosts", dataProviderClass = PostDataProvider.class)
    public void createPostReturns201(String title, String body, int userId) {
        Post payload = Post.builder().title(title).body(body).userId(userId).build();

        postsClient.create(payload)
            .then()
            .statusCode(201)
            .body("title", equalTo(title))
            .body("userId", equalTo(userId))
            .body("id", notNullValue());
    }
}
```

### Auth Tests — `AuthTest.java`

| Test | Expected |
|------|----------|
| Login with valid credentials | 200, token in response |
| Login with missing password | 400, `"error": "Missing password"` |
| Register new user | 200, id + token |
| Register undefined user | 400, error message |

---

## Reporting

Allure report integrates with Jira/TestRail for traceability:

```java
@Test
@TmsLink("TC-001")           // Links to TestRail case
@Issue("BUG-123")             // Links to Jira issue
@Feature("Posts API")
@Story("GET all posts")
@Severity(SeverityLevel.CRITICAL)
public void getAllPostsTest() { ... }
```

Generate report:
```bash
mvn allure:serve   # Opens in browser
mvn allure:report  # Generates to target/site/allure-maven-plugin/
```

---

## CI/CD Integration

```yaml
# .github/workflows/java-tests.yml
name: Java TestNG Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Cache Maven
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
      - run: mvn clean test -Denv=ci
      - uses: actions/upload-artifact@v4
        if: always()
        with:
          name: allure-results
          path: target/allure-results/
```

---

*Part of a QA engineer portfolio. Demonstrates Java backend testing with enterprise-grade tooling.*
