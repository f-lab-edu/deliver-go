package org.deliverygo.order.controller;

import lombok.RequiredArgsConstructor;
import org.deliverygo.order.dto.OrderCreateRequest;
import org.deliverygo.order.service.OrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public void order(@Validated @RequestBody OrderCreateRequest orderCreateRequest) {
        orderService.order(orderCreateRequest);
    }
}
