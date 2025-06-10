package sh.jfm.springbootdemos.restclient;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @ParameterizedTest
    @ValueSource(strings = {"/restTemplate", "/restClient"})
    void getHello_errorsIfMissingName(String path) {
        given()
                .when()
                .get("%s/hello".formatted(path))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getHello_restTemplate_returnsHello() {
        given()
                .queryParam("name", "RestTemplate")
                .when()
                .get("/restTemplate/hello")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("Hello RestTemplate!"));
    }

    @Test
    void getHello_restClient_returnsHello() {
        given()
                .queryParam("name", "RestClient")
                .when()
                .get("/restClient/hello")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("Hello RestClient!"));
    }
}
