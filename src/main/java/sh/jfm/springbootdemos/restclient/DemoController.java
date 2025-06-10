package sh.jfm.springbootdemos.restclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final RestClientDemoHttpClient restClientHttpClient;
    private final RestTemplateDemoHttpClient restTemplateHttpClient;

    public DemoController(
            RestClientDemoHttpClient restClientHttpClient,
            RestTemplateDemoHttpClient restTemplateHttpClient
    ) {
        this.restClientHttpClient = restClientHttpClient;
        this.restTemplateHttpClient = restTemplateHttpClient;
    }

    @GetMapping("/restClient/hello")
    public String getHelloRestClient(@RequestParam String name) {
        return restClientHttpClient.getHello(name);
    }

    @GetMapping("/restTemplate/hello")
    public String getHelloRestTemplate(@RequestParam String name) {
        return restTemplateHttpClient.getHello(name);
    }
}
