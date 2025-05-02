package org.deliverygo.delivery.client;

import com.google.auth.oauth2.GoogleCredentials;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.delivery.dto.GoogleDirectionResponse;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.RetryBackoffSpec;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleDirectionClient {

    private final WebClient webClient;

    private final RetryBackoffSpec googleWebClientBackOff;

    private String accessToken;

    @PostConstruct
    void init() {
        try (InputStream serviceAccountStream = getClass().getClassLoader()
            .getResourceAsStream("fifth-howl-171112-c4473ce9455a.json")) {
            GoogleCredentials credentials = GoogleCredentials
                .fromStream(serviceAccountStream)
                .createScoped("https://www.googleapis.com/auth/cloud-platform");

            credentials.refreshIfExpired();
            accessToken = credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("Google 서비스 계정 인증 파일 읽기 중 예외가 발생했습니다.", e);
        }
    }
    @CircuitBreaker(name = "google-direction", fallbackMethod = "fallbackEta")
    public CompletableFuture<GoogleDirectionResponse> getEta(GoogleEtaRequest googleEtaRequest) {
        return webClient.post()
            .uri("/directions/v2:computeRoutes")
            .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("X-Goog-FieldMask", "routes.duration")
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(googleEtaRequest)
            .retrieve()
            .bodyToMono(GoogleDirectionResponse.class)
            .retryWhen(googleWebClientBackOff)
            .toFuture();
    }

    public CompletableFuture<GoogleDirectionResponse> fallbackEta(GoogleEtaRequest googleEtaRequest, Throwable e) {
        log.error("google api 호출이 실패됐습니다.", e);
        return CompletableFuture.completedFuture(null);
    }
}
