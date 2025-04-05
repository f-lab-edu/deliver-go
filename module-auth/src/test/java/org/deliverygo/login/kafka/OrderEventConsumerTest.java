package org.deliverygo.login.kafka;

import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.login.stomp.StompSendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.deliverygo.global.constants.OrderStatus.RECEIVED;
import static org.deliverygo.global.dto.OrderCreateEvent.OrderSummary;
import static org.deliverygo.global.dto.OrderCreateEvent.UserSummary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"order-topic"})
class OrderEventConsumerTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    KafkaTemplate<String, OrderCreateEvent> kafkaTemplate;

    @MockitoBean
    StompSendService stompSendService;

    String orderTopic = "order-topic";

    @Test
    @DisplayName("Kafka 주문 생성 이벤트 수신 시, StompSendService가 동일한 이벤트 데이터로 호출된다")
    void orderEventTriggersStompService() {
        OrderCreateEvent event = createEvent();

        kafkaTemplate.send(orderTopic, event);

        ArgumentCaptor<OrderCreateEvent> captor = ArgumentCaptor.forClass(OrderCreateEvent.class);
        verify(stompSendService, timeout(5000).times(1)).sendOrderCreateEvent(captor.capture());

        OrderCreateEvent actualEvent = captor.getValue();
        assertEquals(event, actualEvent);
    }

    private OrderCreateEvent createEvent() {
        return new OrderCreateEvent(
            UserSummary.of(1L, "홍길동", "email@test.com"),
            OrderSummary.of(1L, 2L, "식당", "주소", RECEIVED)
        );
    }
}
