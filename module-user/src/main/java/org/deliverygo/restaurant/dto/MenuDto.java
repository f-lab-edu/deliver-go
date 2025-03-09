package org.deliverygo.restaurant.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MenuDto {

    private final String name;

    private final int price;

    private final String description;

    private MenuDto(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static MenuDto of(String name, int price, String description) {
        return new MenuDto(name, price, description);
    }

    public static List<MenuDto> of(List<MenuCreateRequest> requests) {
        return requests.stream()
            .map(request -> new MenuDto(request.name(), request.price(), request.description()))
            .toList();
    }
}
