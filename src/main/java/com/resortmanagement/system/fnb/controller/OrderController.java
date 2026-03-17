package com.resortmanagement.system.fnb.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.fnb.service.OrderService;

@RestController
@RequestMapping("/api/fnb/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<com.resortmanagement.system.fnb.dto.response.OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.fnb.dto.response.OrderResponse> getById(@PathVariable UUID id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new order (and deduct inventory if not simulated)
     * For now, create does it all as per service logic.
     */
    @PostMapping
    public ResponseEntity<com.resortmanagement.system.fnb.dto.response.OrderResponse> create(
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.OrderRequest request) {
        com.resortmanagement.system.fnb.dto.response.OrderResponse saved = orderService.create(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Delete method? Order cancellation logic is complex (revert inventory).
    // Not implemented in service yet.
    // I won't expose delete for now, or returns NotAllowed.
}
