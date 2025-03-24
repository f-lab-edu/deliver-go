package org.deliverygo.order.constants;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    SUCCESS(EnumSet.noneOf(OrderStatus.class)),
    FAILED(EnumSet.noneOf(OrderStatus.class)),
    CANCELED(EnumSet.noneOf(OrderStatus.class)),
    CANCEL_REQUEST(EnumSet.of(CANCELED)),
    RECEIVED(EnumSet.of(SUCCESS, FAILED, CANCEL_REQUEST));

    private final Set<OrderStatus> nextStates;

    OrderStatus(Set<OrderStatus> nextStates) {
        this.nextStates = nextStates;
    }

    public boolean canTransitionTo(OrderStatus next) {
        return nextStates.contains(next);
    }
}
