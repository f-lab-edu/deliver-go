package org.deliverygo.delivery.dto;

public record DeliveryInfoRequest(
    String deliveryId) {

    public static DeliveryInfoRequest of(String deliveryId) {
        return new DeliveryInfoRequest(deliveryId);
    }
}