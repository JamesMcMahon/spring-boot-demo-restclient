package sh.jfm.springbootdemos.restclient;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

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

    @Test
    void getHello_errorsIfClientNotKnown() {
        given()
                .queryParam("client", "unknown")
                .queryParam("name", "test")
                .when()
                .get("/demo/hello")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getHello_errorsIfMissingName() {
        given()
                .queryParam("client", "resttemplate")
                .when()
                .get("/demo/hello")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getHello_restTemplate_returnsHello() {
        given()
                .queryParam("client", "resttemplate")
                .queryParam("name", "RestTemplate")
                .when()
                .get("/demo/hello")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("Hello RestTemplate!"));
    }

    @Test
    void getHello_restClient_returnsHello() {
        given()
                .queryParam("client", "restclient")
                .queryParam("name", "RestClient")
                .when()
                .get("/demo/hello")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("Hello RestClient!"));
    }
}
