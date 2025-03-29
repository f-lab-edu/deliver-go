package org.deliverygo.order.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.global.kafka.KafkaEventProducer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.deliverygo.global.constants.OrderStatus.RECEIVED;
import static org.deliverygo.global.dto.OrderCreateEvent.OrderSummary;
import static org.deliverygo.global.dto.OrderCreateEvent.UserSummary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"order-topic", "order-topic-dlt"})
class OrderEventProducerTest {

    @Autowired
    OrderEventProducer orderEventProducer;

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @SpyBean
    KafkaEventProducer kafkaEventProducer;

    Consumer<String, OrderCreateEvent> consumer;

    String orderTopic = "order-topic";

    String orderTopicDlt = orderTopic + "-dlt";

    @BeforeEach
    void setUp() {
        Map<String, Object> props = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        JsonDeserializer<OrderCreateEvent> valueDeserializer = new JsonDeserializer<>(OrderCreateEvent.class);
        valueDeserializer.addTrustedPackages("*");
        ConsumerFactory<String, OrderCreateEvent> cf = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
            valueDeserializer);
        consumer = cf.createConsumer();
    }

    @AfterEach
    void cleanUp() {
        consumer.close();
    }

    @Test
    @DisplayName("'test-topic' 토픽으로 'hello kafka' 메시지를 보내면, 소비자가 소비한 메시지와 일치")
    void shouldSendOrderCreateEventToOrderTopic() {
        OrderCreateEvent orderEvent = createOrderEvent();

        orderEventProducer.sendOrderCreate(orderEvent);

        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, orderTopic);
        ConsumerRecord<String, OrderCreateEvent> received = KafkaTestUtils.getSingleRecord(consumer, orderTopic);

        assertEquals(orderTopic, received.topic());
        assertEquals(orderEvent, received.value());
    }

    @Test
    @DisplayName("이벤트 전송 실패하면, 'order-topic-dlt' 로 이벤트 전송")
    void shouldSendOrderCreateEventToDltWhenSendFails() {
        OrderCreateEvent orderEvent = createOrderEvent();

        doReturn(CompletableFuture.failedFuture(new RuntimeException("force fail")))
            .when(kafkaEventProducer).send(eq(orderTopic), anyString(), eq(orderEvent));

        orderEventProducer.sendOrderCreate(orderEvent);

        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, orderTopicDlt);
        ConsumerRecord<String, OrderCreateEvent> received = KafkaTestUtils.getSingleRecord(consumer, orderTopicDlt);

        assertEquals(orderTopicDlt, received.topic());
        assertEquals(orderEvent, received.value());
    }

    private OrderCreateEvent createOrderEvent() {
        return new OrderCreateEvent(UserSummary.of(1L, "test1", "test2"),
            OrderSummary.of(1L, 2L, "test1", "test2", RECEIVED));
    }
}
