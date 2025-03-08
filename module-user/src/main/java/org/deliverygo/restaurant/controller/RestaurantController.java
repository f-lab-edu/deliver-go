package org.deliverygo.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.restaurant.dto.RestaurantDto;
import org.deliverygo.restaurant.dto.RestaurantSaveRequest;
import org.deliverygo.restaurant.service.RestaurantService;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.deliverygo.login.constants.UserGrade.*;

@RestController("/api/v1/user")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant")
    public void register(@Validated @RequestBody RestaurantSaveRequest restaurantSaveRequest,
                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken) {
        JwtToken jwtToken = JwtToken.of(accessToken);
        if (jwtToken.extractGrade() != OWNER) {
            throw new IllegalArgumentException("사장님만 음식점을 등록할 수 있습니다.");
        }
        restaurantService.register(RestaurantDto.of(restaurantSaveRequest), Long.valueOf(jwtToken.extractUserId()));
    }
}
