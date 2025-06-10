package sh.jfm.springbootdemos.restclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component("restTemplateHttpbinClient")
public class RestTemplateHttpBinClient implements HttpBinClient {

    private final RestTemplate restTemplate;

    public RestTemplateHttpBinClient(RestTemplateBuilder builder) {
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
}
