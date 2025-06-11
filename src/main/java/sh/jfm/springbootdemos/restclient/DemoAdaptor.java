package sh.jfm.springbootdemos.restclient;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/// Converts raw HTTPBin interactions into domain concerns and hides the HTTP client choice
/// from controllers, keeping the web layer focussed on HTTP inbound concerns only.
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

    public String getError() {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/status/500")
                .build(true)
                .toUri();
        client.getError(uri);
        return "Error Handled!";
    }

    public String getWithAuth(boolean fail) {
        String token = fail ? "this token will fail" : "Bearer fake token";
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/bearer")
                .build(true)
                .toUri();
        String echoedToken = client.getWithToken(uri, token).token();
        return "Authenticated %s!".formatted(echoedToken);
    }

    public String postMessage(ExamplePostRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/post")
                .build(true)
                .toUri();
        String echoed = client.postMessage(uri, request).json().message();
        return "Posted %s!".formatted(echoed);
    }
}
