package org.deliverygo.delivery.client;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.deliverygo.delivery.dto.GoogleDirectionResponse;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Component
@RequiredArgsConstructor
public class GoogleDirectionClient {

    private final WebClient webClient;

    private String accessToken;

    void init() {
        System.out.println("webClient = " + webClient);
        try (FileInputStream serviceAccountStream = new FileInputStream("fifth-howl-171112-c4473ce9455a.json")) {
            GoogleCredentials credentials = GoogleCredentials
                .fromStream(serviceAccountStream)
                .createScoped("https://www.googleapis.com/auth/maps-platform.routes");

            credentials.refreshIfExpired();
            accessToken = credentials.getAccessToken().getTokenValue();
            System.out.println("credentials = " + accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEta(GoogleEtaRequest googleEtaRequest, BiConsumer<Long, Throwable> callback) {
        webClient.post()
            .uri("https://routes.googleapis.com/directions/v2:computeRoutes")
            .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(googleEtaRequest)
            .retrieve()
            .bodyToMono(GoogleDirectionResponse.class)
            .subscribe(
                response -> callback.accept(response.getEtaInSeconds(), null),
                error -> callback.accept(null, error));
    }
}
