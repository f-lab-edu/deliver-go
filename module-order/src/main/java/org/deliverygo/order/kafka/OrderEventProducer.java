package org.deliverygo.order.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.global.kafka.KafkaEventProducer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaEventProducer kafkaEventProducer;

    private final String TOPIC = "order-topic";

    public void sendOrderCreate(OrderCreateEvent orderCreateEvent) {

        String kafkaKey = String.valueOf(orderCreateEvent.getOrderId());
        kafkaEventProducer.send(TOPIC, kafkaKey, orderCreateEvent)
            .exceptionally(e -> {
                log.error("이벤트 전송 실패: topic = {}, event = {}", TOPIC, orderCreateEvent, e);
                sendToDlt(kafkaKey, orderCreateEvent);
                return null;
            });
    }

    private void sendToDlt(String key, OrderCreateEvent payload) {
        String dltTopic = TOPIC + "-dlt";
        kafkaEventProducer.send(dltTopic, key, payload)
            .exceptionally(e -> {
                log.error("dlt 전송 실패: topic = {}, event = {}", TOPIC, payload, e);
                return null;
            });
    }
}
