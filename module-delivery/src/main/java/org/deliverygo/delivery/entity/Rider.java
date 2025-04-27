package org.deliverygo.delivery.entity;


import org.deliverygo.delivery.dto.Location;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rider")
public class Rider {

    @Id
    private String id;
    private String name;
    private Location riderLocation;
    private Location deliveryLocation;
    private Long eta;

    private Rider(String name, Location riderLocation, Location deliveryLocation, Long eta) {
        this.name = name;
        this.riderLocation = riderLocation;
        this.deliveryLocation = deliveryLocation;
        this.eta = eta;
    }

    public static Rider of(SaveDeliveryLocationRequest request, Long eta) {
        return new Rider(request.riderName(), request.riderLocation(), request.deliveryLocation(), eta);
    }

    public static Rider of(SaveDeliveryLocationRequest request) {
        return new Rider(request.riderName(), request.riderLocation(), request.deliveryLocation(), null);
    }
}
