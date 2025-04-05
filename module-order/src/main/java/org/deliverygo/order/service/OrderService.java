package org.deliverygo.order.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.exception.RestaurantCloseException;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.deliverygo.order.dto.OrderCreateRequest;
import org.deliverygo.order.entity.Order;
import org.deliverygo.order.entity.OrderMenu;
import org.deliverygo.order.kafka.OrderCreateEventMapper;
import org.deliverygo.order.kafka.OrderEventProducer;
import org.deliverygo.order.repository.OrderRepository;
import org.deliverygo.restaurant.entity.Menu;
import org.deliverygo.restaurant.entity.Restaurant;
import org.deliverygo.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.deliverygo.order.dto.OrderCreateRequest.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public long order(OrderCreateRequest orderCreateRequest) {
        User user = userRepository.findByEmail(orderCreateRequest.email()).orElseThrow();
        Restaurant restaurant = restaurantRepository.findByIdWithMenus(orderCreateRequest.restaurantId()).orElseThrow();

        isOpen(restaurant);

        List<OrderMenu> orderMenus = createOrderMenus(restaurant, orderCreateRequest);
        Order order = Order.of(user, restaurant, orderMenus, orderCreateRequest);

        Order savedOrder = orderRepository.save(order);

        paymentService.payment(savedOrder);

        orderEventProducer.sendOrderCreate(OrderCreateEventMapper.toDto(user, order));

        return savedOrder.getId();
    }

    private void isOpen(Restaurant restaurant) {
        if (!restaurant.isOpen()) {
            throw new RestaurantCloseException("음식점 " + restaurant.getName() + "가(이) 영업중인 상태가 아닙니다.");
        }
    }

    private List<OrderMenu> createOrderMenus(Restaurant restaurant, OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest
            .menus()
            .stream()
            .map(menuCreateRequest -> createOrderMenu(restaurant, menuCreateRequest))
            .toList();
    }

    private OrderMenu createOrderMenu(Restaurant restaurant, MenuCreateRequest menuCreateRequest) {
        Menu menu = restaurant.findMenu(menuCreateRequest.menuId()).orElseThrow();
        return OrderMenu.of(menu, menuCreateRequest);
    }
}
