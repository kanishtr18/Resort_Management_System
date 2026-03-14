package com.resortmanagement.system.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.service.PurchaseOrderService;

@RestController
@RequestMapping("/api/v1/inventory/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    /**
     * View all purchase orders
     */
    @GetMapping
    public ResponseEntity<List<com.resortmanagement.system.inventory.dto.response.PurchaseOrderResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * View purchase order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.PurchaseOrderResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new purchase order
     */
    @PostMapping
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.PurchaseOrderResponse> create(
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.inventory.dto.request.PurchaseOrderRequest request) {
        com.resortmanagement.system.inventory.dto.response.PurchaseOrderResponse saved = service.create(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Receive purchase order
     * - Updates inventory
     * - Creates InventoryTransaction (IN)
     * - Atomic operation
     */
    @PostMapping("/{id}/receive")
    public ResponseEntity<Void> receive(@PathVariable UUID id) {
        service.receive(id); // Changed from receivePurchaseOrder to receive as per service refactor
        return ResponseEntity.ok().build();
    }
}


