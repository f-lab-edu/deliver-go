package org.deliverygo.order.schduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.global.repository.EventRecordRepository;
import org.deliverygo.order.kafka.OrderEventProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreateMessageScheduler {

    private final EventRecordRepository eventRecordRepository;
    private final OrderEventProducer orderEventProducer;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;

    @Scheduled(cron = "0/10 * * * * *")
    public void resend() {
        eventRecordRepository.findReadEvents(LocalDateTime.now())
            .forEach(record -> {
                try {
                    orderEventProducer.sendOrderCreate(OrderCreateEvent.of(objectMapper, record)).join();
                    transactionTemplate.executeWithoutResult(status ->
                        eventRecordRepository.markDone(record.getId()));
                } catch (Exception e) {
                    log.error("Kafka 전송 실패 또는 상태 업데이트 실패: eventId = {}", record.getId(), e);
                }
            });
    }
}
