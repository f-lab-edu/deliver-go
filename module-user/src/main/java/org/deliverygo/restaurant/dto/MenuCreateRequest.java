package org.deliverygo.restaurant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MenuCreateRequest(

    @NotBlank
    @Size(min = 1, max = 20, message = "메뉴 이름은 최소 1자 이상, 20자 이하로 입력해야 합니다.")
    String name,

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    int price,

    @NotBlank
    @Size(max = 500, message = "메뉴 설명은 최대 500자까지 가능합니다.")
    String description
) {
}
