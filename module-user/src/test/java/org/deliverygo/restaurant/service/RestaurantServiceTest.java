package org.deliverygo.restaurant.service;

import org.deliverygo.MainApplication;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.restaurant.dto.MenuCreateRequest;
import org.deliverygo.restaurant.dto.RestaurantCreateRequest;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.deliverygo.login.constants.UserGrade.OWNER;
import static org.deliverygo.restaurant.constants.RestaurantStatus.*;
import static org.mockito.Mockito.when;

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

    @Mock
    JwtToken jwtToken;

    User owner;

    RestaurantCreateRequest restaurantCreateRequest;

    List<MenuCreateRequest> menus;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(User.ofEncrypt(passwordEncoder, "test", "js.min1", "mjss920415",
            "112222", "22333", OWNER));
        menus = List.of(new MenuCreateRequest("김치볶음밥", 5000, "인천맛집"),
            new MenuCreateRequest("김치볶음밥", 5000, "인천맛집"),
            new MenuCreateRequest("김치볶음밥", 5000, "인천맛집"));
        restaurantCreateRequest = new RestaurantCreateRequest("프랩김밥", "인천광역시", "11112222",
            menus, CLOSE);
    }

    @Test
    @DisplayName("음식점 등록 성공")
    void registerRestaurantSuccess() {
        when(jwtToken.extractUserId()).thenReturn(String.valueOf(owner.getId()));

        Long registeredId = restaurantService.register(restaurantCreateRequest, jwtToken);

        Restaurant findRestaurant = restaurantRepository.findById(registeredId).orElseThrow();
        Assertions.assertEquals(registeredId, findRestaurant.getId());
    }
}
