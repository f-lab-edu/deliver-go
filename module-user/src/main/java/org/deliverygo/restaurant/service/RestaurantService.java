package org.deliverygo.restaurant.service;

import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.restaurant.dto.RestaurantCreateRequest;
import org.deliverygo.restaurant.entity.Menu;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    @Transactional
    public Long register(RestaurantCreateRequest restaurantCreateRequest, JwtToken jwtToken) {
        User owner = userRepository.findById(Long.valueOf(jwtToken.extractUserId())).orElseThrow();

        Restaurant restaurant = Restaurant.of(restaurantCreateRequest, owner);

        addMenus(restaurant, restaurantCreateRequest);

        restaurantRepository.save(restaurant);

        return restaurant.getId();
    }

    private void addMenus(Restaurant restaurant, RestaurantCreateRequest restaurantCreateRequest) {
        restaurantCreateRequest.menus().stream()
            .map(Menu::ofUse)
            .forEach(restaurant::addMenu);
    }
}
