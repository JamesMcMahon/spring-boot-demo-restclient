package sh.jfm.springbootdemos.restclient;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class DemoAdaptor {
    private final HttpBinClient client;
    private final String baseUrl;

    public DemoAdaptor(HttpBinClient client, String baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
    }

    public String getHello(String name) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/get")
                .queryParam("name", name)
                .build(true)
                .toUri();
        String responseName = client.getHello(uri).args().name();
        return "Hello %s!".formatted(responseName);
    }
}
