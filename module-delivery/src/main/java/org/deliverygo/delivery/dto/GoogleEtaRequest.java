package org.deliverygo.delivery.dto;

public record GoogleEtaRequest(
    Origin origin,
    Destination destination) {

    public static GoogleEtaRequest of(SaveDeliveryLocationRequest request) {
        return new GoogleEtaRequest(
            new Origin(new Location(new LatLng(request.riderLocation().latitude(), request.riderLocation().longitude()))),
            new Destination(new Location(new LatLng(request.deliveryLocation().latitude(), request.deliveryLocation().longitude())))
        );
    }

    public record Origin(Location location) {
    }

    public record Destination(Location location) {
    }

    public record Location(LatLng latLng) {
    }

    public record LatLng(double latitude, double longitude) {
    }
}
