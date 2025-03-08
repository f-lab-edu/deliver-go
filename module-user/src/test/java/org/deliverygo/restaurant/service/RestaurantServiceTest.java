package org.deliverygo.restaurant.service;

import org.deliverygo.MainApplication;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.restaurant.dto.RestaurantDto;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.deliverygo.login.constants.UserGrade.OWNER;

@SpringBootTest(classes = MainApplication.class)
@Transactional
class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("음식점 등록 성공")
    void registerRestaurantSuccess() {
        User owner = userRepository.save(User.ofEncrypt(passwordEncoder, "test", "js.min1", "mjss920415",
            "112222", "22333", OWNER));
        RestaurantDto restaurantDto = RestaurantDto.of("프랩김밥", "인천광역시", "11112222");

        Long registeredId = restaurantService.register(restaurantDto, owner.getId());

        Restaurant findRestaurant = restaurantRepository.findById(registeredId).orElseThrow();
        Assertions.assertEquals(registeredId, findRestaurant.getId());
    }
}
