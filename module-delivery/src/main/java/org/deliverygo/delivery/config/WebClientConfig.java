package org.deliverygo.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.time.Duration.ofSeconds;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.reactive.function.client.WebClient.Builder;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient googleWebClient(Builder builder) {
        return builder
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .baseUrl("https://routes.googleapis.com")
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .option(CONNECT_TIMEOUT_MILLIS, 2000)
                    .responseTimeout(ofSeconds(3))))
            .build();
    }
}
