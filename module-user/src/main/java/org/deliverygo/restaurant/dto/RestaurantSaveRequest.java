package org.deliverygo.restaurant.dto;

import org.deliverygo.restaurant.constants.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RestaurantSaveRequest(

    @NotBlank
    @Size(min = 1, max = 20, message = "음식점 이름은 최소 1자 이상, 20자 이하로 입력해야 합니다.")
    String name,

    @NotBlank
    @Size(min = 2, max = 50, message = "주소는 2자 이상, 50자 이하로 입력해야 합니다.")
    String address,

    @NotBlank
    @Pattern(regexp = "^[0-9]{9,11}$", message = "전화번호는 숫자 9~11자리여야 합니다.")
    String phone,
    RestaurantStatus status) {
}
