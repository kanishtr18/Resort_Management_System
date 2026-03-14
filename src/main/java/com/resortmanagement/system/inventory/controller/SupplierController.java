package com.resortmanagement.system.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.service.SupplierService;

@RestController
@RequestMapping("/api/v1/inventory/suppliers")
public class SupplierController {

    private final SupplierService service;

    public SupplierController(SupplierService supplierService) {
        this.service = supplierService;
    }

    /**
     * Get all suppliers
     */
    @GetMapping
    public ResponseEntity<List<com.resortmanagement.system.inventory.dto.response.SupplierResponse>> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        if (activeOnly) {
             return ResponseEntity.ok(service.findAllActive());
        }
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Get supplier by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.SupplierResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create supplier
     */
    @PostMapping
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.SupplierResponse> create(
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.inventory.dto.request.SupplierRequest request) {
        com.resortmanagement.system.inventory.dto.response.SupplierResponse saved = service.create(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Update supplier
     */
    @PutMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.inventory.dto.response.SupplierResponse> update(
            @PathVariable UUID id,
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.inventory.dto.request.SupplierRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    /**
     * Delete supplier
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

