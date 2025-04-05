package org.deliverygo.login.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.login.stomp.StompSendService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final String TOPIC = "order-topic";

    private final StompSendService stompSendService;

    @KafkaListener(topics = TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void orderCreateListen(OrderCreateEvent event) {
        log.debug("이벤트 수신 성공: topic = {}, event = {}", TOPIC, event);
        stompSendService.sendOrderCreateEvent(event);
    }
}
