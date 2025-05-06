package org.deliverygo.delivery.repository;

import org.deliverygo.delivery.entity.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {
}
