package org.deliverygo.global.repository;

import org.deliverygo.global.entity.EventRecord;
import org.springframework.data.repository.CrudRepository;

public interface EventRecordRepository extends CrudRepository<EventRecord, Long> {
}
