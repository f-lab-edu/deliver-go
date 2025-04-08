package org.deliverygo.delivery.dto;

public record SaveDeliveryLocationRequest(

    Location riderLocation,
    Location deliveryLocation
) {
}
