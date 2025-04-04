package org.deliverygo.restaurant.repository;

import org.deliverygo.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus WHERE r.id = :restaurantId")
    Optional<Restaurant> findByIdWithMenus(@Param("restaurantId") long restaurantId);
}
