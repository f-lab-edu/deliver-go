package org.deliverygo.order.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.global.dto.OutboxEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final ApplicationEventPublisher eventPublisher;

    public void publishSaveEvent(OutboxEvent<OrderCreateEvent> event) {
        eventPublisher.publishEvent(event);
    }
}
