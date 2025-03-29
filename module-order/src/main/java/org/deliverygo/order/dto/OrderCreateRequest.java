package org.deliverygo.order.dto;

import jakarta.validation.constraints.NotEmpty;
import org.deliverygo.restaurant.entity.Menu;

import java.util.List;

public record OrderCreateRequest(
    long restaurantId,
    @NotEmpty(message = "메뉴 목록은 최소 1개 이상이어야 합니다.")
    List<MenuCreateRequest> menus,
    String email,
    String address
) {

    public MenuCreateRequest findMenuRequest(Menu menu) {
        return menus.stream()
            .filter(menuCreateRequest -> menu.getId().equals(menuCreateRequest.menuId))
            .findFirst()
            .orElse(null);
    }

    public int calculateTotalPrice() {
        return menus.stream()
            .mapToInt(menuCreateRequest -> menuCreateRequest.menuTotalPrice())
            .sum();
    }

    public record MenuCreateRequest(
        int price,
        long menuId,
        int quantity
    ) {

        public int menuTotalPrice() {
            return price * quantity;
        }
    }
}
