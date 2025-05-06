package org.deliverygo.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.delivery.client.GoogleDirectionClient;
import org.deliverygo.delivery.dto.DeliveryInfoRequest;
import org.deliverygo.delivery.dto.DeliveryInfoResponse;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.deliverygo.delivery.entity.Delivery;
import org.deliverygo.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final GoogleDirectionClient googleDirectionClient;

    private final DeliveryRepository deliveryRepository;

    public void saveDeliveryLocation(SaveDeliveryLocationRequest request) {
        GoogleEtaRequest googleEtaRequest = GoogleEtaRequest.of(request);
        googleDirectionClient.getEta(googleEtaRequest)
            .thenAccept(result ->
                deliveryRepository.save(Delivery.of(request, result.getEtaInSeconds())))
            .exceptionally(e -> {
                log.error("google api 실패로 eta 값 갱신에 실패했습니다.");
                deliveryRepository.save(Delivery.of(request));
                return null;
            });
    }

    public DeliveryInfoResponse getDeliveryInfo(DeliveryInfoRequest deliveryInfoRequest) {
        Delivery rider = deliveryRepository.findById(deliveryInfoRequest.deliveryId()).orElseThrow();
        return DeliveryInfoResponse.from(rider);
    }
}
