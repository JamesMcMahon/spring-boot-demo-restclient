package sh.jfm.springbootdemos.restclient;

import java.net.URI;

public interface HttpBinClient {
    HttpBinGetResponse getHello(URI uri);

    void getError(URI uri);

    HttpBinBearerResponse getWithToken(URI uri, String token);

    HttpBinPostResponse postMessage(URI uri, ExamplePostRequest body);
}
