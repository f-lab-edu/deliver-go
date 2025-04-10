package org.deliverygo.delivery.service;

import static org.deliverygo.global.exception.ErrorType.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.delivery.client.GoogleDirectionClient;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.deliverygo.delivery.entity.Rider;
import org.deliverygo.delivery.repository.RiderRepository;
import org.deliverygo.global.exception.BusinessException;
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
                throw new BusinessException(GOOGLE_API_ERROR, e);
            });
    }
}
