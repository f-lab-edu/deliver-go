package org.deliverygo.restaurant.service;

import org.deliverygo.restaurant.dto.RestaurantDto;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    @Transactional
    public Long register(RestaurantDto restaurantDto, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow();

        Restaurant restaurant = Restaurant.of(restaurantDto, owner);

        restaurantRepository.save(restaurant);

        return restaurant.getId();
    }
}
