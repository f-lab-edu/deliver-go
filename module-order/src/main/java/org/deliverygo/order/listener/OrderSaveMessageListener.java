package org.deliverygo.order.listener;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.global.dto.OutboxEvent;
import org.deliverygo.global.repository.EventRecordRepository;
import org.deliverygo.order.kafka.OrderEventProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class OrderSaveMessageListener {

    private final OrderEventProducer orderEventProducer;
    private final EventRecordRepository eventRecordRepository;
    private final TransactionTemplate transactionTemplate;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void publishMessage(OutboxEvent<OrderCreateEvent> event) {
        orderEventProducer.sendOrderCreate(event.getPayload())
            .thenRun(() ->
                transactionTemplate.executeWithoutResult(status ->
                    eventRecordRepository.markDone(event.getEventId())));
    }
}
