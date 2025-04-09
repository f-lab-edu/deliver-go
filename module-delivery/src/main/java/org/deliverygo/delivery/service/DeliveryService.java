package org.deliverygo.delivery.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.delivery.client.GoogleDirectionClient;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.deliverygo.delivery.entity.Rider;
import org.deliverygo.delivery.repository.RiderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final GoogleDirectionClient googleDirectionClient;

    private final RiderRepository riderRepository;

    public void saveDeliveryLocation(SaveDeliveryLocationRequest request) {
        GoogleEtaRequest googleEtaRequest = GoogleEtaRequest.of(request);

        googleDirectionClient.getEta(googleEtaRequest, (eta, error) ->
            riderRepository.saveRiderLocation(Rider.of(request, eta)));
    }



    // 협력
    // 컨트롤러가 서비스에게 라이더 위치 저장하라고 요청
    // Delivery 서비스가 비즈니스로직을 조율한다.
    // 서비스가 ewt 정보를 요청 요청
    // 서비스가 mongoDB 저장하라고 요청

    // 메시지
    // controller -> service : 라이더id, 라이더 위치 정보, 배달위치정보,
    // service -> webClient: 라이더 위치정보, 배달위치정보
    // service -> DbClient: 라이더id, 라이더 위치 정보, 예상도착시간
    // 라이더 위치정보, 예상도착시간 전달

    // 객체
    // webClient: openFeign,
    // dbClient:
}
