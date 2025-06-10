package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final DemoService restClientDemoService;
    private final DemoService restTemplateDemoService;

    public DemoController(
            @Qualifier("restClientDemoService") DemoService restClientDemoService,
            @Qualifier("restTemplateDemoService") DemoService restTemplateDemoService
    ) {
        this.restClientDemoService = restClientDemoService;
        this.restTemplateDemoService = restTemplateDemoService;
    }

    @GetMapping("/restClient/hello")
    public String getHelloRestClient(@RequestParam String name) {
        return restClientDemoService.getHello(name);
    }

    @GetMapping("/restTemplate/hello")
    public String getHelloRestTemplate(@RequestParam String name) {
        return restTemplateDemoService.getHello(name);
    }
}
