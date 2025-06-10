package sh.jfm.springbootdemos.restclient;

import java.net.URI;

public interface HttpBinClient {
    HttpBinGetResponse getHello(URI uri);

    void getError(URI uri);
}
