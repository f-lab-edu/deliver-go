package org.deliverygo.delivery.dto;

public record SaveDeliveryLocationRequest(
    String riderName,
    Location riderLocation,
    Location deliveryLocation
) {
}
