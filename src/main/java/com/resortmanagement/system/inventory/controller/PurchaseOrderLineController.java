package com.resortmanagement.system.inventory.controller;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.service.PurchaseOrderLineService;

@RestController
@RequestMapping("/api/inventory/purchase-orders/{purchaseOrderId}/lines")
public class PurchaseOrderLineController {

    private final PurchaseOrderLineService service;

    public PurchaseOrderLineController(PurchaseOrderLineService service) {
        this.service = service;
    }

    /**
     * Get all lines for a purchase order
     */
    @GetMapping
    public ResponseEntity<@Nullable Object> getLinesByPurchaseOrder(
            @PathVariable UUID purchaseOrderId) {

        return ResponseEntity.ok(
                service.findByPurchaseOrderId(purchaseOrderId));
    }
}
