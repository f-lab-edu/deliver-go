package org.deliverygo.delivery.service;

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
}
