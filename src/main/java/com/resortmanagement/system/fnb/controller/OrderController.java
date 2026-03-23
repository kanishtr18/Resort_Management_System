package com.resortmanagement.system.fnb.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.common.enums.OrderStatus;
import com.resortmanagement.system.fnb.dto.response.OrderResponse;
import com.resortmanagement.system.fnb.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;

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
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<List<com.resortmanagement.system.fnb.dto.response.OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<com.resortmanagement.system.fnb.dto.response.OrderResponse> create(
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.OrderRequest request) {
        com.resortmanagement.system.fnb.dto.response.OrderResponse saved = orderService.create(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
public ResponseEntity<OrderResponse> updateStatus(
        @PathVariable UUID id,
        @RequestParam OrderStatus status) {
    return ResponseEntity.ok(orderService.updateStatus(id, status));
}
    // Delete method? Order cancellation logic is complex (revert inventory).
    // Not implemented in service yet.
    // I won't expose delete for now, or returns NotAllowed.
}
