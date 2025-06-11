package sh.jfm.springbootdemos.restclient;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(DemoController.class)
@Import({RestClientHttpBinClient.class, RestTemplateHttpBinClient.class})
@EnableWireMock
class DemoControllerMockServerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getError_restClient_returnsAnErrorIfThereIsNoError() {
        stubFor(get(urlEqualTo("/status/500"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        var message = assertThrows(ServletException.class, () ->
                mockMvc.perform(MockMvcRequestBuilders.get("/restClient/error"))
        ).getMessage();
        assertThat(message, containsString("Unexpected status code"));
    }

    @Test
    void getError_restTemplate_returnsAnErrorIfThereIsNoError() {
        stubFor(get(urlEqualTo("/status/500"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        var message = assertThrows(ServletException.class, () ->
                mockMvc.perform(MockMvcRequestBuilders.get("/restTemplate/error"))
        ).getMessage();
        assertThat(message, containsString("Unexpected status code"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Qualifier("restClientDemoAdaptor")
        public DemoAdaptor restClientDemoAdaptor(
                @Qualifier("restClientHttpBinClient") HttpBinClient client,
                @Value("${wiremock.server.baseUrl}") String baseUrl
        ) {
            return new DemoAdaptor(client, baseUrl);
        }

        @Bean
        @Qualifier("restTemplateDemoAdaptor")
        public DemoAdaptor restTemplateDemoAdaptor(
                @Qualifier("restTemplateHttpBinClient") HttpBinClient client,
                @Value("${wiremock.server.baseUrl}") String baseUrl
        ) {
            return new DemoAdaptor(client, baseUrl);
        }

        @Bean
        public RestClient.Builder builder() {
            return RestClient.builder();
        }

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }
    }
}
