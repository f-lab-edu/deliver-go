package org.deliverygo.restaurant.dto;


import org.deliverygo.restaurant.constants.RestaurantStatus;
import lombok.Getter;

import static org.deliverygo.restaurant.constants.RestaurantStatus.CLOSE;

@Getter
public class RestaurantDto {

    private final String name;

    private final String address;

    private final String phone;

    private final RestaurantStatus status;

    private RestaurantDto(String name, String address, String phone, RestaurantStatus status) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.status = status;
    }

    public static RestaurantDto of(String name, String address, String phone) {
        return new RestaurantDto(name, address, phone, CLOSE);
    }

    public static RestaurantDto of(RestaurantSaveRequest request) {
        return new RestaurantDto(request.name(), request.address(), request.phone(), CLOSE);
    }
}
