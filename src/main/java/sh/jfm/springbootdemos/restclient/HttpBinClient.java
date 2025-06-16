package sh.jfm.springbootdemos.restclient;

import java.net.URI;

/// Interface for the [HTTPBin](https://httpbin.org/) endpoints used in the demo.
/// Two independent implementations exist:
/// * [RestClientHttpBinClient] – uses the modern, fluent and immutable
///   [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
/// * [RestTemplateHttpBinClient] – uses the older, mutable
///   [RestTemplate](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate)
///
/// Controllers/adaptors depend on this abstraction, not on the concrete HTTP API.
public interface HttpBinClient {

    /// GET /get?name= – echoes the query string back.
    HttpBinGetResponse getHello(URI uri);

    /// GET /status/500 – must NOT throw; 500 is the expected happy-path here.
    void getError(URI uri);

    /// POST /post – JSON body is echoed under `.json`.
    HttpBinPostResponse postMessage(URI uri, ExamplePostRequest body);

    /// GET /bearer – translate HTTP 401 into [UnauthorizedException](UnauthorizedException.java).
    HttpBinBearerResponse getWithToken(URI uri, String token);
}
