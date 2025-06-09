package sh.jfm.springbootdemos.restclient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

    /* all DemoHttpClient beans keyed by their bean-name ("resttemplate","restclient",…) */
    private final Map<String, DemoHttpClient> clients;

    public DemoController(Map<String, DemoHttpClient> clients) {
        this.clients = clients;
    }

    @GetMapping("/hello")
    public String getHello(@RequestParam String client,
                           @RequestParam String name) {
        DemoHttpClient demoHttpClient = clients.get(client.toLowerCase());
        if (demoHttpClient == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported client");
        }
        return "Hello %s!".formatted(demoHttpClient.getHello(name));
    }
}
