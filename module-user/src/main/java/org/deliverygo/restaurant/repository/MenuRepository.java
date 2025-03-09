package org.deliverygo.restaurant.repository;

import org.deliverygo.restaurant.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
