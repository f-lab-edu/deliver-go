package org.deliverygo.order.service;

import org.deliverygo.exception.RestaurantCloseException;
import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.order.dto.OrderCreateRequest;
import org.deliverygo.order.entity.Order;
import org.deliverygo.order.kafka.OrderEventProducer;
import org.deliverygo.order.repository.OrderRepository;
import org.deliverygo.restaurant.entity.Menu;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.MenuRepository;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.deliverygo.order.dto.OrderCreateRequest.MenuCreateRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@EmbeddedKafka
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuRepository menuRepository;

    @MockitoBean
    OrderEventProducer orderEventProducer;

    @Test
    @DisplayName("10번 음식점 '왕돈까스집' 이 Open 상태일 때 주문을 하면, 총 금액과, 메뉴 갯수, 음식점 이름이 요청한 정보와 DB 간에 일치")
    void shouldSuccessfullyPlaceOrderWhenRestaurantIsOpen() {

        //case
        Restaurant openRestaurant = createOpenRestaurant(createUser());
        mockingRepository(createUser(), openRestaurant);
        OrderCreateRequest orderCreateRequest = createOrderCreateRequest(createMenuCreateRequests(openRestaurant));

        //when
        long orderedId = orderService.order(orderCreateRequest);
        Order savedOrder = orderRepository.findById(orderedId).orElseThrow();

        //then
        assertEquals("왕돈까스집", savedOrder.getRestaurant().getName());
        assertEquals(orderCreateRequest.calculateTotalPrice(), savedOrder.getTotalPrice());
        assertEquals(2, savedOrder.getOrderMenus().size());
    }

    @Test
    @DisplayName("음식점이 Close 상태일 때 주문을 하면, 예외 발생")
    void shouldThrowExceptionWhenRestaurantIsClosed() {

        //case
        Restaurant openRestaurant = createOpenRestaurant(createUser());
        OrderCreateRequest orderCreateRequest = createOrderCreateRequest(createMenuCreateRequests(openRestaurant));
        mockingRepository(createUser(), createCloseRestaurant(createUser()));

        //then
        assertThrows(RestaurantCloseException.class, () -> orderService.order(orderCreateRequest));
    }

    private User createUser() {
        return User.of("testPassword", "사용자", "js.test.com", "인천 서구",
            "01022223333", UserGrade.NORMAL);
    }

    private Restaurant createOpenRestaurant(User user) {
        Restaurant restaurant = Restaurant.ofOpen("왕돈까스집", "인천 열미", "01022222222", user);
        restaurant.addMenu(menuRepository.save(Menu.of("이름1", new BigDecimal(5000), "test1")));
        restaurant.addMenu(menuRepository.save(Menu.of("이름2", new BigDecimal(10000), "test2")));
        return restaurant;
    }

    private OrderCreateRequest createOrderCreateRequest(List<MenuCreateRequest> menus) {
        return new OrderCreateRequest(10, menus, "js.test.com", "서울");
    }

    private void mockingRepository(User user, Restaurant restaurant) {
        when(userRepository.findByEmail("js.test.com")).thenReturn(Optional.of(user));
        when(restaurantRepository.findByIdWithMenus(10L)).thenReturn(Optional.of(restaurant));
    }

    private Restaurant createCloseRestaurant(User user) {
        return Restaurant.ofClose("왕돈까스집", "인천 열미", "01022222222", user);
    }

    private List<MenuCreateRequest> createMenuCreateRequests(Restaurant restaurant) {
        return restaurant.getMenus()
            .stream()
            .map(menu -> new MenuCreateRequest(menu.getPrice(), menu.getId(), 3))
            .toList();
    }
}
