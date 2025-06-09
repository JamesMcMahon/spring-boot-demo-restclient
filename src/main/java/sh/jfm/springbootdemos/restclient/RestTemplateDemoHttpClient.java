package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service("resttemplate")
public class RestTemplateDemoHttpClient implements DemoHttpClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RestTemplateDemoHttpClient(
            RestTemplateBuilder builder,
            @Value("${httpbin.base-url}") String baseUrl
    ) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;
    }

    @Override
    public String getHello(String name) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/get")
                .queryParam("name", name)
                .build(true)
                .toUri();
        return restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        HttpBinGetResponse.class
                ).getBody()
                .args().name();
    }
}
