package org.deliverygo.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.login.entity.BaseEntity;
import org.deliverygo.login.entity.User;
import org.deliverygo.global.constants.OrderStatus;
import org.deliverygo.order.dto.OrderCreateRequest;
import org.deliverygo.restaurant.entity.Restaurant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static org.deliverygo.global.constants.OrderStatus.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    private String deliveryAddress;

    private Order(User user, Restaurant restaurant, String deliveryAddress) {
        this.user = user;
        this.restaurant = restaurant;
        this.orderTime = LocalDateTime.now();
        this.status = RECEIVED;
        this.deliveryAddress = deliveryAddress;
    }

    public static Order of(User user, Restaurant restaurant, List<OrderMenu> orderMenus, OrderCreateRequest createRequest) {
        Order order = new Order(user, restaurant, createRequest.address());
        orderMenus.
            forEach(orderMenu -> order.addMenu(orderMenu));
        return order;
    }

    private void addMenu(OrderMenu orderMenu) {
        orderMenu.assignOrder(this);
        orderMenus.add(orderMenu);
        totalPrice = totalPrice.add(orderMenu.calculatePrice());
    }
}
