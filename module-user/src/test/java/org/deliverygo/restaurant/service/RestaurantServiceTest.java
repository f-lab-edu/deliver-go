package org.deliverygo.restaurant.service;

import org.deliverygo.MainApplication;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.restaurant.dto.RestaurantDto;
import org.deliverygo.restaurant.dto.RestaurantSaveRequest;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    @DisplayName("음식점 등록 성공")
    void registerRestaurantSuccess() {
        RestaurantSaveRequest request = new RestaurantSaveRequest("프랩김밥", "인천광역시", "11112222");
        User owner = userRepository.save(User.of("test", "js.min1", "mjss920415",
            "112222", "22333", OWNER));

        Long registeredId = restaurantService.register(RestaurantDto.ofClose(request), owner.getId());

        Restaurant findRestaurant = restaurantRepository.findById(registeredId).orElseThrow();
        Assertions.assertEquals(registeredId, findRestaurant.getId());
    }
}
