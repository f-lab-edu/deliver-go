package org.deliverygo.order.dto;

import jakarta.validation.constraints.NotEmpty;
import org.deliverygo.restaurant.entity.Menu;

import java.math.BigDecimal;
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

    public BigDecimal calculateTotalPrice() {
        return menus.stream()
            .map(menuCreateRequest -> menuCreateRequest.menuTotalPrice())
            .reduce(BigDecimal.ZERO, (acc, value) -> acc.add(value));
    }

    public record MenuCreateRequest(
        BigDecimal price,
        long menuId,
        int quantity
    ) {

        public BigDecimal menuTotalPrice() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
