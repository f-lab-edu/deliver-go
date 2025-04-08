package org.deliverygo.delivery.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.delivery.client.GoogleDirecitonClient;
import org.deliverygo.delivery.dto.GoogleEtaRequest;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final GoogleDirecitonClient googleDirecitonClient;

    public void saveDeliveryLocation(SaveDeliveryLocationRequest request) {
        GoogleEtaRequest googleEtaRequest = GoogleEtaRequest.of(request);

        googleDirecitonClient.getEta(googleEtaRequest, (eta, error) ->
            dbRepository.saveDeliveryLocation(request, eta));
        // redis? mongodb?
    }

    // 협력(문맥) -> 메시지(책임) -> 객체 -> 행동 -> 상태 -> 클래스

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
