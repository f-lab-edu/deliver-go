package org.deliverygo.restaurant.service;

import org.deliverygo.restaurant.dto.MenuDto;
import org.deliverygo.restaurant.dto.RestaurantDto;
import org.deliverygo.restaurant.entity.Menu;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.MenuRepository;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private final MenuRepository menuRepository;

    @Transactional
    public Long register(RestaurantDto restaurantDto, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow();

        Restaurant restaurant = Restaurant.of(restaurantDto, owner);

        restaurantRepository.save(restaurant);

        addMenus(restaurant, restaurantDto.getMenus());

        menuRepository.saveAll(restaurant.getMenus());

        return restaurant.getId();
    }

    private void addMenus(Restaurant restaurant, List<MenuDto> menuDtoList) {
        menuDtoList.stream()
            .map(Menu::ofUse)
            .forEach(restaurant::addMenu);
    }
}
