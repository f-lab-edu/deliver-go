package org.deliverygo.delivery.dto;

public record SaveDeliveryLocationRequest(
    String deliveryId,
    String riderName,
    Location riderLocation,
    Location deliveryLocation
) {
}
