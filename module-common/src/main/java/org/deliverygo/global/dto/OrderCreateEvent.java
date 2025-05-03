package org.deliverygo.global.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.deliverygo.global.constants.OrderStatus;
import org.deliverygo.global.entity.EventRecord;

public record OrderCreateEvent(
    UserSummary userSummary,
    OrderSummary orderSummary
) {

    public Long getOrderId() {
        return orderSummary.orderId();
    }

    public static OrderCreateEvent of(UserSummary userSummary, OrderSummary orderSummary) {
        return new OrderCreateEvent(userSummary, orderSummary);
    }

    public static OrderCreateEvent of(ObjectMapper objectMapper, EventRecord record) {
        OutboxEvent<OrderCreateEvent> outboxEvent = objectMapper.convertValue(
            record.getPayload(), new TypeReference<>() {
            });
        return outboxEvent.getPayload();
    }

    public record UserSummary(
        Long userId,
        String userName,
        String userAddress
    ) {

        public static UserSummary of(Long id, String name, String address) {
            return new UserSummary(id, name, address);
        }
    }

    public record OrderSummary(
        Long orderId,
        Long restaurantId,
        String restaurantName,
        String restaurantAddress,
        OrderStatus orderStatus
    ) {

        public static OrderSummary of(Long orderId, Long restaurantId, String restaurantName, String address,
                                      OrderStatus status) {
            return new OrderSummary(orderId, restaurantId, restaurantName, address, status);
        }
    }
}
