package org.deliverygo.order.service;

import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.order.dto.OrderCreateRequest;
import org.deliverygo.order.entity.Order;
import org.deliverygo.order.repository.OrderRepository;
import org.deliverygo.restaurant.entity.Menu;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.MenuRepository;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.deliverygo.order.dto.OrderCreateRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    RestaurantRepository restaurantRepository;

    @MockitoBean
    MenuRepository menuRepository;

    @Test
    @DisplayName("음식점이 Open이면 주문 성공")
    void shouldSuccessfullyPlaceOrderWhenRestaurantIsOpen() {
        ArrayList<MenuCreateRequest> menus = new ArrayList<>();
        menus.add(new MenuCreateRequest(5000, 5L, 3));
        menus.add(new MenuCreateRequest(10000, 6L,5));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(10, menus, "js.test.com", "서울");
        User user = User.of("testPassword", "사용자", "js.test.com", "인천 서구",
            "01022223333", UserGrade.NORMAL);

        Restaurant restaurant = Restaurant.ofOpen("왕돈까스집", "인천 열미", "01022222222", user);
        when(userRepository.findByEmail("js.test.com")).thenReturn(Optional.of(user));

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(5L)).thenReturn(Optional.of(Menu.of("이름1", 5000, "test1")));
        when(menuRepository.findById(6L)).thenReturn(Optional.of(Menu.of("이름2", 10000, "test2")));

        long orderedId = orderService.order(orderCreateRequest);

        Order savedOrder = orderRepository.findById(orderedId).orElseThrow();

        assertEquals("왕돈까스집", savedOrder.getRestaurant().getName());
        assertEquals((5000 * 3) + (10000 * 5), savedOrder.getTotalPrice());
        assertEquals(2, savedOrder.getOrderMenus().size());
    }

    @Test
    @DisplayName("음식점이 close 이면 주문 예외")
    void shouldThrowExceptionWhenRestaurantIsClosed() {
        ArrayList<MenuCreateRequest> menus = new ArrayList<>();
        menus.add(new MenuCreateRequest(5000, 5L, 3));
        menus.add(new MenuCreateRequest(10000, 6L,5));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(10, menus, "js.test.com", "서울");
        User user = User.of("testPassword", "사용자", "js.test.com", "인천 서구",
            "01022223333", UserGrade.NORMAL);

        Restaurant restaurant = Restaurant.ofClose("왕돈까스집", "인천 열미", "01022222222", user);
        when(userRepository.findByEmail("js.test.com")).thenReturn(Optional.of(user));

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(5L)).thenReturn(Optional.of(Menu.of("이름1", 5000, "test1")));
        when(menuRepository.findById(6L)).thenReturn(Optional.of(Menu.of("이름2", 10000, "test2")));

        assertThrows(IllegalStateException.class, () -> orderService.order(orderCreateRequest));
    }
}
