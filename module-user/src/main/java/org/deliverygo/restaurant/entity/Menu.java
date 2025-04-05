package org.deliverygo.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.login.entity.BaseEntity;
import org.deliverygo.restaurant.dto.MenuCreateRequest;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    private boolean useFlag;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private BigDecimal price;

    @Column(length = 500)
    private String description;

    private Menu(String name, boolean useFlag, BigDecimal price, String description) {
        this.name = name;
        this.useFlag = useFlag;
        this.price = price;
        this.description = description;
    }

    private Menu(Long id, String name, boolean useFlag, BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.useFlag = useFlag;
        this.price = price;
        this.description = description;
    }

    public static Menu of(Long id, String name, BigDecimal price, String description) {
        return new Menu(id, name, true, price, description);
    }

    public static Menu of(String name, BigDecimal price, String description) {
        return new Menu(name, true, price, description);
    }

    public static Menu ofUse(MenuCreateRequest menuCreateRequest) {
        return new Menu(menuCreateRequest.name(), true, menuCreateRequest.price(), menuCreateRequest.description());
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
