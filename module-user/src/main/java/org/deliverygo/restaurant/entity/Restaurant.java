package org.deliverygo.restaurant.entity;

import lombok.NoArgsConstructor;
import org.deliverygo.restaurant.constants.RestaurantStatus;
import org.deliverygo.restaurant.dto.MenuDto;
import org.deliverygo.restaurant.dto.RestaurantDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.deliverygo.login.entity.BaseEntity;
import org.deliverygo.login.entity.User;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "restaurant_id")
    private long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false, length = 11)
    private String phone;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "restaurant", fetch = LAZY, cascade = ALL)
    private List<Menu> menus = new ArrayList<>();

    @Enumerated(STRING)
    private RestaurantStatus status;

    private Restaurant(String name, String address, String phone, User owner, RestaurantStatus status) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.owner = owner;
        this.status = status;
    }

    public static Restaurant of(RestaurantDto restaurantDto, User owner) {
        Restaurant restaurant = new Restaurant(restaurantDto.getName(),
            restaurantDto.getAddress(),
            restaurantDto.getPhone(),
            owner,
            restaurantDto.getStatus());

        for (MenuDto menuDto : restaurantDto.getMenus()) {
            restaurant.addMenus(Menu.ofUse(menuDto));
        }

        return restaurant;
    }

    public void addMenus(Menu menu) {
        this.menus.add(menu);
        menu.setRestaurant(this);
    }
}
