package org.deliverygo.delivery.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.delivery.dto.Location;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "delivery")
@NoArgsConstructor
@Getter
public class Delivery {

    @Id
    private String id;
    private String name;
    private Location riderLocation;
    private Location deliveryLocation;
    private Long eta;

    private Delivery(String id, String name, Location riderLocation, Location deliveryLocation, Long eta) {
        this.id = id;
        this.name = name;
        this.riderLocation = riderLocation;
        this.deliveryLocation = deliveryLocation;
        this.eta = eta;
    }

    private Delivery(String name, Location riderLocation, Location deliveryLocation, Long eta) {
        this.name = name;
        this.riderLocation = riderLocation;
        this.deliveryLocation = deliveryLocation;
        this.eta = eta;
    }

    public static Delivery of(SaveDeliveryLocationRequest request, Long eta) {
        return new Delivery(request.deliveryId() ,request.riderName(), request.riderLocation(), request.deliveryLocation(), eta);
    }

    public static Delivery of(SaveDeliveryLocationRequest request) {
        return new Delivery(request.deliveryId(), request.riderName(), request.riderLocation(), request.deliveryLocation(), null);
    }
}
