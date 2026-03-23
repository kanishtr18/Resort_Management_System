package com.resortmanagement.system.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.dto.response.PurchaseOrderLineResponse;
import com.resortmanagement.system.inventory.service.PurchaseOrderLineService;

@RestController
@RequestMapping("/api/inventory/purchase-orders/{purchaseOrderId}/lines")
public class PurchaseOrderLineController {

    private final PurchaseOrderLineService service;

    public PurchaseOrderLineController(PurchaseOrderLineService service) {
        this.service = service;
    }

    // Fix: was @Nullable Object — now proper List<PurchaseOrderLineResponse>
    @GetMapping
    @PreAuthorize("hasAnyAuthority('inventory:view', 'inventory:manage', 'ADMIN')")
    public ResponseEntity<List<PurchaseOrderLineResponse>> getLinesByPurchaseOrder(
            @PathVariable UUID purchaseOrderId) {
        return ResponseEntity.ok(service.findByPurchaseOrderId(purchaseOrderId));
    }
}