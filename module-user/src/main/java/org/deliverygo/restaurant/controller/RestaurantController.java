package org.deliverygo.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.restaurant.dto.RestaurantCreateRequest;
import org.deliverygo.restaurant.service.RestaurantService;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.deliverygo.login.constants.UserGrade.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/user/restaurant")
    public void register(@Validated @RequestBody RestaurantCreateRequest restaurantCreateRequest,
                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken) {
        JwtToken jwtToken = JwtToken.of(accessToken);
        if (jwtToken.extractGrade() != OWNER) {
            throw new IllegalStateException("사장님만 음식점을 등록할 수 있습니다.");
        }

        restaurantService.register(restaurantCreateRequest, Long.valueOf(jwtToken.extractUserId()));
    }
}
