package org.deliverygo.order.listener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.global.helper.EventRecordFactory;
import org.deliverygo.global.repository.EventRecordRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderSaveRecordListener {

    private final EventRecordRepository eventRecordRepository;
    private final EventRecordFactory eventRecordFactory;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void saveRecord(OrderCreateEvent event) {
        eventRecordRepository.save(eventRecordFactory.create(event));
    }
}
