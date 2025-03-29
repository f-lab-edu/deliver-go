package org.deliverygo.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.login.entity.BaseEntity;
import org.deliverygo.restaurant.entity.Menu;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static org.deliverygo.order.dto.OrderCreateRequest.*;

@Entity
@Table(name = "order_menu")
@Getter
@NoArgsConstructor
public class OrderMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_menu_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "quantity")
    private int quantity;

    private OrderMenu(Menu menu, int quantity) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public static OrderMenu of(Menu menu, MenuCreateRequest menuCreateRequest) {
        return new OrderMenu(menu, menuCreateRequest.quantity());
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public BigDecimal calculatePrice() {
        return menu.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
