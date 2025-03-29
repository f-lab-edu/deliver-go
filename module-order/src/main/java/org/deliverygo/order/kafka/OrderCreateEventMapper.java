package org.deliverygo.order.kafka;

import org.deliverygo.global.dto.OrderCreateEvent;
import org.deliverygo.login.entity.User;
import org.deliverygo.order.entity.Order;

import static org.deliverygo.global.dto.OrderCreateEvent.*;

public class OrderCreateEventMapper {

    public static OrderCreateEvent toDto(User user, Order order) {
        return OrderCreateEvent.of(
            UserSummary.of(user.getId(), user.getName(), user.getAddress()),
            OrderSummary.of(order.getId(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getRestaurant().getAddress(),
                order.getStatus()));
    }
}
