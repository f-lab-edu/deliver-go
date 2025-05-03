package org.deliverygo.global.repository;

import org.deliverygo.global.entity.EventRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRecordRepository extends CrudRepository<EventRecord, Long> {

    @Query("SELECT e FROM EventRecord e WHERE e.status = 'READY' AND e.createdAt <= :threshold")
    List<EventRecord> findReadEvents(@Param("threshold") LocalDateTime localDateTime);

    @Modifying
    @Query("UPDATE EventRecord e SET e.status = 'DONE' WHERE e.id = :eventId")
    void markDone(@Param("eventId") Long eventId);
}
