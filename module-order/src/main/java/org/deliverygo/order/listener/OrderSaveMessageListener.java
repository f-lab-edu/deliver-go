package org.deliverygo.order.listener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.order.kafka.OrderEventProducer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderSaveMessageListener {

    private final OrderEventProducer orderEventProducer;

    @Async(value = "orderMessageExecutor")
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void publishMessage(OrderCreateEvent orderCreateEvent) {
        orderEventProducer.sendOrderCreate(orderCreateEvent);
    }
}
