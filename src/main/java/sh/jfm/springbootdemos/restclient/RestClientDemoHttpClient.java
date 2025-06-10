package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestClientDemoHttpClient implements DemoHttpClient {

    private final RestClient restClient;

    public RestClientDemoHttpClient(
            RestClient.Builder builder,
            @Value("${httpbin.base-url}") String baseUrl
    ) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public String getHello(String name) {
        URI uri = UriComponentsBuilder.fromUriString("/get")
                .queryParam("name", name)
                .build(true)
                .toUri();
        String responseName = restClient.get()
                .uri(uri)
                .retrieve()
                .body(HttpBinGetResponse.class)
                .args().name();
        return "Hello %s!".formatted(responseName);
    }
}
