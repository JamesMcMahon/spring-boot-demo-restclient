package sh.jfm.springbootdemos.restclient;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/// Integration tests for the DemoController using [TestContainers](https://testcontainers.com/) and [REST-Assured](https://rest-assured.io/).
/// This test suite verifies the behavior of both [RestTemplate](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate)
///  and [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient) implementations
/// by testing against a containerized [HTTPBin](https://httpbin.org/) instance.
///
/// The test class uses:
/// - Spring Boot Test with random port allocation
/// - TestContainers for managing the HTTPBin container
/// - REST Assured for HTTP request testing
/// - Parameterized tests to verify both REST implementations
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DemoControllerIntegrationTest {

    @Container
    static GenericContainer<?> httpbin =
            new GenericContainer<>("kennethreitz/httpbin:latest")
                    .withExposedPorts(80);
    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void httpbinBaseUrl(DynamicPropertyRegistry registry) {
        registry.add(
                "httpbin.base-url",
                () -> "http://%s:%d".formatted(httpbin.getHost(), httpbin.getMappedPort(80)));
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void getHello_errorsIfMissingName(String path) {
        given()
                .when()
                .get("%s/hello".formatted(path))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void getHello_returnsHello(String path) {
        given()
                .queryParam("name", "Test")
                .when()
                .get("%s/hello".formatted(path))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Hello Test!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void getError_returnsHandled(String path) {
        given()
                .when()
                .get("%s/error".formatted(path))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Error Handled!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void post_returnsPosted(String path) {
        given()
                .body("{\"message\":\"Test Message\"}")
                .contentType("application/json")
                .when()
                .post("%s/post".formatted(path))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Posted Test Message!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void getAuth_returnsAuthenticated(String path) {
        given()
                .when()
                .get("%s/auth".formatted(path))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Authenticated fake token!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void getAuth_fail_returnsHandled(String path) {
        given()
                .when()
                .get("%s/auth?fail".formatted(path))
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body(equalTo("Authentication Error Handled!"));
    }
}
