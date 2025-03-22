package org.deliverygo.login.stomp;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.dto.OrderCreateEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StompSendService {

    public void sendOrderCreateEvent(OrderCreateEvent event) {
    }
}
