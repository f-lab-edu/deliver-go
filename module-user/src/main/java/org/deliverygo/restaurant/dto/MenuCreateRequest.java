package org.deliverygo.restaurant.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record MenuCreateRequest(

    @NotBlank
    @Size(min = 1, max = 20, message = "메뉴 이름은 최소 1자 이상, 20자 이하로 입력해야 합니다.")
    String name,

    @DecimalMin(value = "0.00", message = "가격은 0원 이상이어야 합니다.")
    BigDecimal price,

    @NotBlank
    @Size(max = 500, message = "메뉴 설명은 최대 500자까지 가능합니다.")
    String description
) {
}
