package com.hustict.aims.controller;

import com.hustict.aims.dto.order.RushOrderRequestDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import com.hustict.aims.service.RushOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PlaceRushOrderController {
    private final RushOrderService rushOrderService;

    @Autowired
    public PlaceRushOrderController(RushOrderService rushOrderService) {
        this.rushOrderService = rushOrderService;
    }

    @PostMapping("/place-rush-order")
    public ResponseEntity<RushOrderResponseDTO> placeRushOrder(@RequestBody RushOrderRequestDTO request) {
        RushOrderResponseDTO response = rushOrderService.processRushOrder(request);
        return ResponseEntity.ok(response);
    }
} 