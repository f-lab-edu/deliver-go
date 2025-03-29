package org.deliverygo.restaurant.entity;

import lombok.NoArgsConstructor;
import org.deliverygo.restaurant.constants.RestaurantStatus;
import org.deliverygo.restaurant.dto.RestaurantCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import org.deliverygo.login.entity.BaseEntity;
import org.deliverygo.login.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static org.deliverygo.restaurant.constants.RestaurantStatus.*;

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

    public static Restaurant of(RestaurantCreateRequest restaurantCreateRequest, User owner) {
        return new Restaurant(restaurantCreateRequest.name(),
            restaurantCreateRequest.address(),
            restaurantCreateRequest.phone(),
            owner,
            restaurantCreateRequest.status());
    }

    public static Restaurant ofOpen(String name, String address, String phone, User owner) {
        return new Restaurant(name, address, phone, owner, OPEN);
    }

    public static Restaurant ofClose(String name, String address, String phone, User owner) {
        return new Restaurant(name, address, phone, owner, CLOSE);
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.setRestaurant(this);
    }

    public boolean isOpen() {
        return status == OPEN;
    }

    public Optional<Menu> findMenu(long menuId) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst();
    }
}
