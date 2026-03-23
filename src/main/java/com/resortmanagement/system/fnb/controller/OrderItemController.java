package com.resortmanagement.system.fnb.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.fnb.dto.response.OrderItemResponse;
import com.resortmanagement.system.fnb.service.OrderItemService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/fnb/order-items")
public class OrderItemController {

    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    // Fix: return List<OrderItemResponse> instead of @Nullable Object
    @GetMapping
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<List<OrderItemResponse>> getAllActive() {
        return ResponseEntity.ok(service.findAllActive());
    }

    // Fix: return OrderItemResponse DTO instead of raw OrderItem entity
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<OrderItemResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Note: POST and PUT removed — OrderItems are managed via OrderService (cascade).
    // Exposing direct create/update for OrderItem breaks aggregate encapsulation.
}