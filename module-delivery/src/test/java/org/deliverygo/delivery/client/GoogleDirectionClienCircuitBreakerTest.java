package org.deliverygo.delivery.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.Location;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.RetryBackoffSpec;

import java.util.concurrent.ExecutionException;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.CLOSED;
import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.util.retry.Retry.backoff;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=localhost:9092")
class GoogleDirectionClienCircuitBreakerTest {

    static MockWebServer mockWebServer;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    GoogleDirectionClient googleDirectionClient;

    @TestConfiguration
    static class TestConfig {

        @Bean(name = "testWebClient")
        @Primary
        public WebClient webClient() {
            return WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        }

        @Bean(name = "testGoogleWebClientBackOff")
        @Primary
        public RetryBackoffSpec googleWebClientBackOff() {
            return backoff(0, ofSeconds(0)).maxBackoff(ofSeconds(0));
        }
    }

    @BeforeAll
    static void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("google api 호출 5번 중 5번 실패(100%) 시, circuitbreaker open")
    void test1() throws InterruptedException, ExecutionException {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("google-direction");
        //given
        for (int i = 0; i < 5; i++) {
            mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        }
        GoogleEtaRequest request = createRequest();
        //when
        for (int i = 0; i < 5; i++) {
            googleDirectionClient.getEta(request).get();
        }
        //then
        assertEquals(OPEN, circuitBreaker.getState());
    }

    @Test
    @DisplayName("circuitbreaker open 상태에서 3초 후에, api 성공 시 circuitbreaker close")
    void test2() throws InterruptedException, ExecutionException {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("google-direction");
        for (int i = 0; i < 5; i++) {
            mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        }
        GoogleEtaRequest request = createRequest();
        for (int i = 0; i < 5; i++) {
            googleDirectionClient.getEta(request).get();
        }
        Thread.sleep(3100);
        assertEquals(OPEN, circuitBreaker.getState());

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
            .setBody("{\"routes\": [{\"duration\": \"3600s\"}]}")
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE));
        googleDirectionClient.getEta(request).get();

        assertEquals(CLOSED, circuitBreaker.getState());
    }

    private GoogleEtaRequest createRequest() {
        return GoogleEtaRequest.of(new SaveDeliveryLocationRequest(
            "test-rider", new Location(224.51, -20.5), new Location(21.51, 224.5)));
    }
}
