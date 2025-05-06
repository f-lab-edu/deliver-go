package org.deliverygo.delivery.dto;

import lombok.Getter;
import org.deliverygo.delivery.entity.Delivery;

@Getter
public class DeliveryInfoResponse {

    private final String name;
    private final Location riderLocation;
    private final Long eta;

    private DeliveryInfoResponse(String name, Location riderLocation, Long eta) {
        this.name = name;
        this.riderLocation = riderLocation;
        this.eta = eta;
    }

    public static DeliveryInfoResponse from(Delivery delivery) {
        return new DeliveryInfoResponse(delivery.getName(), delivery.getRiderLocation(), delivery.getEta());
    }
}
