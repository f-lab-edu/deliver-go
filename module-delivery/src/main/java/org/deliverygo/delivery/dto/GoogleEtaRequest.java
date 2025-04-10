package org.deliverygo.delivery.dto;

public record GoogleEtaRequest(
    Origin origin,
    Destination destination,
    String travelMode,
    String routingPreference,
    String departureTime) {

//    private String travelMode = "DRIVE";
//    private String routingPreference = "TRAFFIC_AWARE";
//    private String departureTime; // ISO 형식 e.g. "2025-04-09T09:00:00Z"

    public static GoogleEtaRequest of(SaveDeliveryLocationRequest request) {
        return new GoogleEtaRequest(new Origin(
            new Location(new LatLng(request.riderLocation().latitude(), request.riderLocation().longitude()))),
            new Destination(
                new Location(new LatLng(request.riderLocation().latitude(), request.riderLocation().longitude()))),
            "DRIVE",
            "TRAFFIC_AWARE",
            "2025-04-10T14:30:00Z");
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
