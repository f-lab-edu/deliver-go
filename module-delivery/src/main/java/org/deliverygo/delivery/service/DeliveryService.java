package org.deliverygo.delivery.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.delivery.client.GoogleDirectionClient;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.deliverygo.delivery.entity.Rider;
import org.deliverygo.delivery.repository.RiderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final GoogleDirectionClient googleDirectionClient;

    private final RiderRepository riderRepository;

    @CircuitBreaker(name = "google-direction", fallbackMethod = "fallbackEta")
    public void saveDeliveryLocation(SaveDeliveryLocationRequest request) {
        GoogleEtaRequest googleEtaRequest = GoogleEtaRequest.of(request);

        googleDirectionClient.getEta(googleEtaRequest)
            .thenAccept(result ->
                riderRepository.save(Rider.of(request, result.getEtaInSeconds())))
            .exceptionally(e -> {
                log.error("google api 실패로 eta 값 갱신에 실패했습니다.");
                riderRepository.save(Rider.of(request));
                return null;
            });
    }

    private void fallbackEta(SaveDeliveryLocationRequest request, Throwable e) {
        log.error("google api 서버 장애로 인해 api 호출을 차단합니다.", e);
        riderRepository.save(Rider.of(request));
    }
}
