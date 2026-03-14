package com.resortmanagement.system.inventory.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.service.InventoryItemService;

@RestController
@RequestMapping("/api/v1/inventory/items")
public class InventoryItemController {

    private final InventoryItemService service;

    public InventoryItemController(InventoryItemService service) {
        this.service = service;
    }

    /**
     * Get all inventory items
     * Optional: lowStock=true
     */
    @GetMapping
    public ResponseEntity<List<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse>> getAll(
            @RequestParam(value = "lowStock", defaultValue = "false") boolean lowStock) {

        if (lowStock) {
            return ResponseEntity.ok(service.findLowStockItems());
        }
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Get inventory item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create inventory item
     */
    @PostMapping
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> create(
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.inventory.dto.request.InventoryItemRequest request) {
        com.resortmanagement.system.inventory.dto.response.InventoryItemResponse saved = service.create(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> update(
            @PathVariable UUID id,
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.inventory.dto.request.InventoryItemRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    /**
     * Adjust inventory quantity (atomic)
     * Example: +5 or -2.5 (baseUnit enforced)
     */
    @PatchMapping("/{id}/adjust")
    public ResponseEntity<Void> adjustQuantity(
            @PathVariable UUID id,
            @RequestParam BigDecimal quantityDelta) {
        service.adjustQuantity(id, quantityDelta);
        return ResponseEntity.ok().build();
    }

    /**
     * Hard delete inventory item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

