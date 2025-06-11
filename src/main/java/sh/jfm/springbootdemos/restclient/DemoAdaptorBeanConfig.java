package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoAdaptorBeanConfig {

    /// [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient) flavor
    @Bean
    @Qualifier("restClientDemoAdaptor")
    public DemoAdaptor restClientDemoAdaptor(
            @Qualifier("restClientHttpBinClient") HttpBinClient client,
            @Value("${httpbin.base-url}") String baseUrl
    ) {
        return new DemoAdaptor(client, baseUrl);
    }

    /// [RestTemplate](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate) flavor
    @Bean
    @Qualifier("restTemplateDemoAdaptor")
    public DemoAdaptor restTemplateDemoAdaptor(
            @Qualifier("restTemplateHttpBinClient") HttpBinClient client,
            @Value("${httpbin.base-url}") String baseUrl
    ) {
        return new DemoAdaptor(client, baseUrl);
    }
}
