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
}
