package org.deliverygo.delivery.dto;

import lombok.Getter;

import java.util.List;

public class GoogleDirectionResponse {
    private List<Route> routes;

    public long getEtaInSeconds() {
        if (routes != null && !routes.isEmpty()) {
            Duration duration = routes.get(0).getDuration();
            return duration != null ? duration.getSeconds() : 0;
        }
        return 0;
    }

    @Getter
    public static class Route {
        private Duration duration;
        // getter/setter
    }

    @Getter
    public static class Duration {
        private long seconds;
        // getter/setter
    }
}
