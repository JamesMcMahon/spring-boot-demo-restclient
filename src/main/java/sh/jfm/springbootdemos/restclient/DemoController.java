package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static String handleUnauthorizedException() {
        return "Authentication Error Handled!";
    }

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

    @PostMapping("/restClient/post")
    public String postRestClient(@RequestBody ExamplePostRequest body) {
        return restClientDemoAdaptor.postMessage(body);
    }

    @PostMapping("/restTemplate/post")
    public String postRestTemplate(@RequestBody ExamplePostRequest body) {
        return restTemplateDemoAdaptor.postMessage(body);
    }

    @GetMapping("/restClient/auth")
    public String getAuthRestClient(@RequestParam(name = "fail", required = false) String fail) {
        return restClientDemoAdaptor.getWithAuth(fail != null);
    }

    @GetMapping("/restTemplate/auth")
    public String getAuthRestTemplate(@RequestParam(name = "fail", required = false) String fail) {
        return restTemplateDemoAdaptor.getWithAuth(fail != null);
    }
}
