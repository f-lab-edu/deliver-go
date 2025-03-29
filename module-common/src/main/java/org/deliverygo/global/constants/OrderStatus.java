package org.deliverygo.global.constants;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    SUCCESS,
    FAILED,
    CANCELED,
    CANCEL_REQUEST,
    RECEIVED;

    private Set<OrderStatus> nextStates;

    static {
        SUCCESS.nextStates = EnumSet.noneOf(OrderStatus.class);
        FAILED.nextStates = EnumSet.noneOf(OrderStatus.class);
        CANCELED.nextStates = EnumSet.noneOf(OrderStatus.class);
        CANCEL_REQUEST.nextStates = EnumSet.of(CANCELED);
        RECEIVED.nextStates = EnumSet.of(SUCCESS, FAILED, CANCEL_REQUEST);
    }

    public boolean canTransitionTo(OrderStatus next) {
        return nextStates.contains(next);
    }
}
