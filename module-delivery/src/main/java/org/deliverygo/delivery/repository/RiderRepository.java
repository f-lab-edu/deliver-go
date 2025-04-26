package org.deliverygo.delivery.repository;

import org.deliverygo.delivery.entity.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RiderRepository extends MongoRepository<Rider, String> {
}
