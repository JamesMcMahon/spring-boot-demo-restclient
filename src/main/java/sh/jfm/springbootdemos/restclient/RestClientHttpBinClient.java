package sh.jfm.springbootdemos.restclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;

/// [HTTPBin](https://httpbin.org/) implementation based on Spring 6
/// [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient).
/// RestClient is immutable and thread-safe – build once and reuse.
/// Its fluent DSL lets us specify, execute and decode the request in one chain.
/// Compare with [RestTemplateHttpBinClient](RestTemplateHttpBinClient.java).
@Component("restClientHttpBinClient")
public class RestClientHttpBinClient implements HttpBinClient {

    private final RestClient restClient;

    public RestClientHttpBinClient(RestClient.Builder builder) {
        // Build a single immutable RestClient instance – safe for concurrent reuse
        this.restClient = builder.build();
    }

    @Override
    public HttpBinGetResponse getHello(URI uri) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(HttpBinGetResponse.class);
    }

    @Override
    public void getError(URI uri) {
        // 500 is expected from HTTPBin /status/500 – suppress default error handling
        restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(status -> status.value() == 500,
                        (_, _) -> {
                            // Do nothing for 500 errors - allow them to pass through without throwing an exception
                        })
                .onStatus(status -> status.value() != 500,
                        (_, response) -> {
                            // All other status codes are unexpected for this error scenario should throw an exception
                            throw new RestClientResponseException(
                                    "Unexpected status code",
                                    response.getStatusCode().value(),
                                    response.getStatusText(),
                                    response.getHeaders(),
                                    null,
                                    null
                            );
                        })
                .toBodilessEntity();
    }

    @Override
    public HttpBinBearerResponse getWithToken(URI uri, String token) {
        return restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(status -> status.isSameCodeAs(HttpStatus.UNAUTHORIZED), (_, _) -> {
                    throw new UnauthorizedException();
                })
                .toEntity(HttpBinBearerResponse.class).getBody();
    }

    @Override
    public HttpBinPostResponse postMessage(URI uri, ExamplePostRequest body) {
        return restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(HttpBinPostResponse.class);
    }
}
