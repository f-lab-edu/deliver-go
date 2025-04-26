package org.deliverygo.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.deliverygo.delivery.dto.SaveDeliveryLocationRequest;
import org.deliverygo.delivery.service.DeliveryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/riders/{riderId}/location")
    public void saveRiderLocation(@RequestBody SaveDeliveryLocationRequest request) {
        deliveryService.saveDeliveryLocation(request);
    }
}
