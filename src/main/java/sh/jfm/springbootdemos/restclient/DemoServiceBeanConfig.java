package sh.jfm.springbootdemos.restclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoServiceBeanConfig {

    @Bean
    @Qualifier("restClientDemoService")
    public DemoService restClientDemoService(
            @Qualifier("restClientHttpbinClient") HttpBinClient client,
            @Value("${httpbin.base-url}") String baseUrl
    ) {
        return new DemoService(client, baseUrl);
    }

    @Bean
    @Qualifier("restTemplateDemoService")
    public DemoService restTemplateDemoService(
            @Qualifier("restTemplateHttpbinClient") HttpBinClient client,
            @Value("${httpbin.base-url}") String baseUrl
    ) {
        return new DemoService(client, baseUrl);
    }
}
