package sh.jfm.springbootdemos.restclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/// [HTTPBin](https://httpbin.org/) implementation using legacy
/// [RestTemplate](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate).
/// RestTemplate is still supported but in maintenance mode; new code should
/// migrate to [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient).
/// This class exists purely for comparison.
@Component("restTemplateHttpBinClient")
public class RestTemplateHttpBinClient implements HttpBinClient {

    private final RestTemplateBuilder builder;
    private final RestTemplate restTemplate;

    public RestTemplateHttpBinClient(RestTemplateBuilder builder) {
        this.builder = builder;
        this.restTemplate = builder.build();
    }

    @Override
    public HttpBinGetResponse getHello(URI uri) {
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                HttpBinGetResponse.class
        ).getBody();
    }

    @Override
    public void getError(URI uri) {
        // create a specific RestTemplate for our error handler to avoid applying to ALL calls in the future
        RestTemplate localRestTemplate = builder.build();
        // Custom handler: treat 500 as success so the demo can handle it manually
        localRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @SuppressWarnings("NullableProblems")
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                // 500 errors are expected for this error scenario should not throw an exception
                return !response.getStatusCode().isSameCodeAs(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                throw new RestClientResponseException(
                        "Unexpected status code",
                        response.getStatusCode().value(),
                        response.getStatusText(),
                        response.getHeaders(),
                        null,
                        null
                );
            }
        });
        localRestTemplate.getForEntity(uri, Void.class);
    }

    @Override
    public HttpBinPostResponse postMessage(URI uri, ExamplePostRequest body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExamplePostRequest> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                HttpBinPostResponse.class
        ).getBody();
    }

    @Override
    public HttpBinBearerResponse getWithToken(URI uri, String token) {
        // create local RestTemplate so only this call gets the special 401 handling
        RestTemplate local = builder.build();
        local.setErrorHandler(new DefaultResponseErrorHandler() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                    throw new UnauthorizedException();
                }
                super.handleError(url, method, response);
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return local.exchange(uri, HttpMethod.GET, entity, HttpBinBearerResponse.class).getBody();
    }
}
