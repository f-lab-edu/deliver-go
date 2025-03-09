package org.deliverygo.restaurant.dto;


import org.deliverygo.restaurant.constants.RestaurantStatus;
import lombok.Getter;

import java.util.List;

import static org.deliverygo.restaurant.constants.RestaurantStatus.CLOSE;

@Getter
public class RestaurantDto {

    private final String name;

    private final String address;

    private final String phone;

    private final RestaurantStatus status;

    private final List<MenuDto> menus;

    private RestaurantDto(String name, String address, String phone, RestaurantStatus status, List<MenuDto> menus) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.menus = menus;
    }

    public static RestaurantDto of(String name, String address, String phone, List<MenuDto> menus) {
        return new RestaurantDto(name, address, phone, CLOSE, menus);
    }

    public static RestaurantDto of(RestaurantCreateRequest request) {
        return new RestaurantDto(request.name(), request.address(), request.phone(), CLOSE, MenuDto.of(request.menus()));
    }
}
