package org.deliverygo.global.kafka;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final KafkaTemplate<String, OrderCreateEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, OrderCreateEvent>> send(String topic, String key, OrderCreateEvent value) {
        return kafkaTemplate.send(topic, key, value);
    }
}
