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
        return new GoogleEtaRequest();
    }

    public static class Origin {
        private Location location;
        // getter/setter
    }

    public static class Destination {
        private Location location;
    }

    public static class Location {
        private LatLng latLng;
    }

    public static class LatLng {
        private double latitude;
        private double longitude;
    }
}
