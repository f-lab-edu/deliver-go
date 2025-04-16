package org.deliverygo.delivery.client;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.deliverygo.delivery.dto.GoogleDirectionResponse;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Component
@RequiredArgsConstructor
public class GoogleDirectionClient {

    private final WebClient webClient;

    private String accessToken;

    @PostConstruct
    void init() {
        try (InputStream serviceAccountStream =  getClass().getClassLoader().getResourceAsStream("fifth-howl-171112-c4473ce9455a.json")) {
            GoogleCredentials credentials = GoogleCredentials
                .fromStream(serviceAccountStream)
                .createScoped("https://www.googleapis.com/auth/cloud-platform");

            credentials.refreshIfExpired();
            accessToken = credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<GoogleDirectionResponse> getEta(GoogleEtaRequest googleEtaRequest) {
        return webClient.post()
            .uri("https://routes.googleapis.com/directions/v2:computeRoutes")
            .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("X-Goog-FieldMask", "routes.duration")
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(googleEtaRequest)
            .retrieve()
            .bodyToMono(GoogleDirectionResponse.class)
            .toFuture();
    }
}
