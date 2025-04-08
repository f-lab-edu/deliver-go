package org.deliverygo.delivery.client;

import lombok.RequiredArgsConstructor;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.BiConsumer;

@Component
@RequiredArgsConstructor
public class GoogleDirecitonClient {

    private final WebClient webClient;

    public long getEta(GoogleEtaRequest googleEtaRequest, BiConsumer<Long, Throwable> callback) {
        return webClient.get()
            .uri(builder -> builder
                .path()
                .queryParam()
                .queryParam()
                .queryParam()
                .build())
            .retrieve()
            .bodyToMono(GoogleDirectionResponse.class)
            .map(GoogleDirectionResponse::getEta)
            .subscribe(
                eta -> callback.accept(eta, null),
                error -> callback.accept(null, error)
            );
    }
}
