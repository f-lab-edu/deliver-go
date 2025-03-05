package org.deliverygo.restaurant.dto;

import org.deliverygo.restaurant.constants.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static org.deliverygo.restaurant.constants.RestaurantStatus.*;

@Getter
public class RestaurantSaveRequest {

    @NotBlank
    @Size(min = 1, max = 20, message = "음식점 이름은 최소 1자 이상, 20자 이하로 입력해야 합니다.")
    private final String name;

    @NotBlank
    @Size(min = 2, max = 50, message = "주소는 2자 이상, 50자 이하로 입력해야 합니다.")
    private final String address;

    @NotBlank
    @Pattern(regexp = "^[0-9]{9,11}$", message = "전화번호는 숫자 9~11자리여야 합니다.")
    private final String phone;

    private final RestaurantStatus status;

    public RestaurantSaveRequest(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.status = CLOSE;
    }
}
