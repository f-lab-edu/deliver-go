package org.deliverygo.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.deliverygo.restaurant.constants.RestaurantStatus;

import java.util.List;

public record RestaurantCreateRequest(

    @NotBlank
    @Size(min = 1, max = 20, message = "음식점 이름은 최소 1자 이상, 20자 이하로 입력해야 합니다.")
    String name,

    @NotBlank
    @Size(min = 2, max = 50, message = "주소는 2자 이상, 50자 이하로 입력해야 합니다.")
    String address,

    @NotBlank
    @Pattern(regexp = "^[0-9]{9,11}$", message = "전화번호는 숫자 9~11자리여야 합니다.")
    String phone,

    @Valid
    @NotEmpty(message = "메뉴 목록은 최소 1개 이상이어야 합니다.")
    List<MenuCreateRequest> menus,

    RestaurantStatus status) {
}
