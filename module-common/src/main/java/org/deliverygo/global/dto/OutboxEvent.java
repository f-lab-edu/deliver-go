package org.deliverygo.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.global.entity.EventRecord;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Getter
@NoArgsConstructor
public class OutboxEvent<T> implements ResolvableTypeProvider {

    private Long eventId;
    private T payload;

    private OutboxEvent(T payload) {
        this.payload = payload;
    }

    public static <T> OutboxEvent<T> of(T payload) {
        return new OutboxEvent<>(payload);
    }

    public void assignEventId(EventRecord eventRecord) {
        this.eventId = eventRecord.getId();
    }

    @Override
    @JsonIgnore
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(payload));
    }
}
