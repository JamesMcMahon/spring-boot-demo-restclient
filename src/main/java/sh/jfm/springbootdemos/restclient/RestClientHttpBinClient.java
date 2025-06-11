package sh.jfm.springbootdemos.restclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;

@Component("restClientHttpbinClient")
public class RestClientHttpBinClient implements HttpBinClient {

    private final RestClient restClient;

    public RestClientHttpBinClient(RestClient.Builder builder) {
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
    public HttpBinPostResponse postMessage(URI uri, ExamplePostRequest body) {
        return restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(HttpBinPostResponse.class);
    }
}
