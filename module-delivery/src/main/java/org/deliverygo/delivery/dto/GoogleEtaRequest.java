package org.deliverygo.delivery.dto;

public record GoogleEtaRequest(
    Origin origin,
    Destination destination) {

    public static GoogleEtaRequest of(SaveDeliveryLocationRequest request) {
        return new GoogleEtaRequest(
            new Origin(new Location(LatLng.ofRiderLocation(request))),
            new Destination(new Location(LatLng.ofDeliveryLocation(request))));
    }

    public record Origin(Location location) {
    }

    public record Destination(Location location) {
    }

    public record Location(LatLng latLng) {
    }

    public record LatLng(double latitude, double longitude) {

        public static LatLng ofRiderLocation(SaveDeliveryLocationRequest request) {
            return new LatLng(request.riderLocation().latitude(), request.riderLocation().longitude());
        }

        public static LatLng ofDeliveryLocation(SaveDeliveryLocationRequest request) {
            return new LatLng(request.deliveryLocation().latitude(), request.deliveryLocation().longitude());
        }
    }
}
