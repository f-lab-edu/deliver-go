package org.deliverygo.delivery.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.deliverygo.delivery.dto.GoogleDirectionResponse;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.Location;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.util.retry.Retry.backoff;

class GoogleDirectionClientRetryTest {

    MockWebServer mockWebServer;

    GoogleDirectionClient googleDirectionClient;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build();

        googleDirectionClient = new GoogleDirectionClient(webClient,
            backoff(3, ofSeconds(1)).maxBackoff(ofSeconds(5)));
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("google api 호출 에러 시 retry 3회 후, 최종 실패")
    void test1() {
        //given
        for (int i = 0; i < 4; i++) {
            mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        }
        GoogleEtaRequest request = createRequest();
        //when
        CompletableFuture<GoogleDirectionResponse> response = googleDirectionClient.getEta(request);
        //then
        Assertions.assertThrows(ExecutionException.class, () -> response.get());
    }

    @Test
    @DisplayName("google api 호출 에러 시 retry 1회 후, 성공")
    void test2() throws ExecutionException, InterruptedException {
        //given
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
            .setBody("{\"routes\": [{\"duration\": \"3600s\"}]}")
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE));
        GoogleEtaRequest request = createRequest();
        //when
        CompletableFuture<GoogleDirectionResponse> future = googleDirectionClient.getEta(request);

        //then
        GoogleDirectionResponse response = future.get();
        assertEquals(3600, response.getEtaInSeconds());
    }

    private  GoogleEtaRequest createRequest() {
        return GoogleEtaRequest.of(new SaveDeliveryLocationRequest(
            "test-rider", new Location(224.51, -20.5), new Location(21.51, 224.5)));
    }
}
