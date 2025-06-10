package sh.jfm.springbootdemos.restclient;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
}
