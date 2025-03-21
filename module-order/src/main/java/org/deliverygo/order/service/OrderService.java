package org.deliverygo.order.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.exception.RestaurantCloseException;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.order.dto.OrderCreateRequest;
import org.deliverygo.order.entity.Order;
import org.deliverygo.order.entity.OrderMenu;
import org.deliverygo.order.repository.OrderRepository;
import org.deliverygo.restaurant.entity.Menu;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.MenuRepository;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final PaymentService paymentService;

    @Transactional
    public long order(OrderCreateRequest orderCreateRequest) {
        User user = userRepository.findByEmail(orderCreateRequest.email()).orElseThrow();
        Restaurant restaurant = restaurantRepository.findById(orderCreateRequest.restaurantId()).orElseThrow();

        isOpen(restaurant);

        List<OrderMenu> orderMenus = createOrderMenus(orderCreateRequest);
        Order order = Order.of(user, restaurant, orderMenus, orderCreateRequest);

        Order savedOrder = orderRepository.save(order);

        paymentService.payment(savedOrder);

        return savedOrder.getId();
    }

    private void isOpen(Restaurant restaurant) {
        if(!restaurant.isOpen()) {
            throw new RestaurantCloseException("음식점 " + restaurant.getName() + "가(이) 영업중인 상태가 아닙니다.");
        }
    }

    private List<OrderMenu> createOrderMenus(OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.menus()
            .stream()
            .map(menuCreateRequest -> createOrderMenu(menuCreateRequest))
            .toList();
    }

    private OrderMenu createOrderMenu(OrderCreateRequest.MenuCreateRequest menuCreateRequest) {
        Menu menu = menuRepository.findById(menuCreateRequest.menuId()).orElseThrow();
        return OrderMenu.of(menu, menuCreateRequest);
    }
}
