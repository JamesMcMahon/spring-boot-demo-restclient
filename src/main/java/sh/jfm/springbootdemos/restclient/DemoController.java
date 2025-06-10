package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final DemoAdaptor restClientDemoAdaptor;
    private final DemoAdaptor restTemplateDemoAdaptor;

    public DemoController(
            @Qualifier("restClientDemoAdaptor") DemoAdaptor restClientDemoAdaptor,
            @Qualifier("restTemplateDemoAdaptor") DemoAdaptor restTemplateDemoAdaptor
    ) {
        this.restClientDemoAdaptor = restClientDemoAdaptor;
        this.restTemplateDemoAdaptor = restTemplateDemoAdaptor;
    }

    @GetMapping("/restClient/hello")
    public String getHelloRestClient(@RequestParam String name) {
        return restClientDemoAdaptor.getHello(name);
    }

    @GetMapping("/restTemplate/hello")
    public String getHelloRestTemplate(@RequestParam String name) {
        return restTemplateDemoAdaptor.getHello(name);
    }

    @GetMapping("/restClient/error")
    public String getErrorRestClient() {
        return restClientDemoAdaptor.getError();
    }

    @GetMapping("/restTemplate/error")
    public String getErrorRestTemplate() {
        return restTemplateDemoAdaptor.getError();
    }
}
