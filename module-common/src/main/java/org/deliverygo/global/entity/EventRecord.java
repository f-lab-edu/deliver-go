package org.deliverygo.global.entity;

import static jakarta.persistence.EnumType.*;
import static org.deliverygo.global.constants.EventRecordStatus.*;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.global.constants.EventRecordStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor
public class EventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private EventRecordStatus status;

    @Column(name = "payload", columnDefinition = "json", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode payload;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    private EventRecord(JsonNode payload, EventRecordStatus status) {
        this.payload = payload;
        this.status = status;
    }

    public static EventRecord of(JsonNode jsonNode) {
        return new EventRecord(jsonNode, READY);
    }
}
