package sh.jfm.springbootdemos.restclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;


@Component("restTemplateHttpbinClient")
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
        localRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @SuppressWarnings("NullableProblems")
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                // 500 errors are expected for this error scenario should not throw an exception
                return response.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR;
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
}
