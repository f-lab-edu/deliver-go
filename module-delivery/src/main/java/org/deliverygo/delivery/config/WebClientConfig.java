package org.deliverygo.delivery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.RetryBackoffSpec;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.time.Duration.ofSeconds;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.reactive.function.client.WebClient.Builder;
import static reactor.util.retry.Retry.backoff;

@Configuration
public class WebClientConfig {

    @Value("${google-direction.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${google-direction.retry.backoff-seconds:1}")
    private long backoffSeconds;

    @Value("${google-direction.retry.max-backoff-seconds:1}")
    private long maxBackoffSeconds;

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

    @Bean
    public RetryBackoffSpec googleWebClientBackOff() {
        return backoff(maxAttempts, ofSeconds(backoffSeconds)).maxBackoff(ofSeconds(maxBackoffSeconds));
    }
}
