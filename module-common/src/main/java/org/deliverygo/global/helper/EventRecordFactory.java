package org.deliverygo.global.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.deliverygo.global.entity.EventRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRecordFactory {

    private final ObjectMapper objectMapper;

    public EventRecord create(Object object) {
        JsonNode jsonNode = objectMapper.valueToTree(object);
        return EventRecord.of(jsonNode);
    }
}
