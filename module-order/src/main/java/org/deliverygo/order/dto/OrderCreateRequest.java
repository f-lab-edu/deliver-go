package org.deliverygo.order.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(

    long restaurantId,

    @NotEmpty(message = "메뉴 목록은 최소 1개 이상이어야 합니다.")
    List<MenuCreateRequest> menus,
    String email,
    String address
) {
    public record MenuCreateRequest(

        int price,
        long menuId,
        int quantity
    ) {
    }
}
