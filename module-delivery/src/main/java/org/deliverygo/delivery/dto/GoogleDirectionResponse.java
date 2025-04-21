package org.deliverygo.delivery.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GoogleDirectionResponse {
    private List<Route> routes;

    public long getEtaInSeconds() {
        if (routes != null && !routes.isEmpty()) {
            String duration = routes.getFirst().duration;
            if (duration != null && duration.endsWith("s")) {
                return Long.parseLong(duration.replace("s", ""));
            }
        }
        return 0L;
    }

    @Getter
    public static class Route {
        private String duration;
    }
}
