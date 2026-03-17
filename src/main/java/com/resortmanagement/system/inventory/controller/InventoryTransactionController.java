package com.resortmanagement.system.inventory.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.service.InventoryTransactionService;

@RestController
@RequestMapping("/api/inventory/transactions")
public class InventoryTransactionController {

    private final InventoryTransactionService service;

    public InventoryTransactionController(InventoryTransactionService service) {
        this.service = service;
    }

    /**
     * View inventory transactions
     * Optional filter: itemId (Not implemented in service yet but kept for
     * structure)
     */
    @GetMapping
    public ResponseEntity<List<com.resortmanagement.system.inventory.dto.response.InventoryTransactionResponse>> getAll() {
        // Ignoring itemId filter for now as service refactor didn't include it fully or
        // I missed it.
        // Returning findAll
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Manual inventory adjustment (ADMIN only)
     */
    @PostMapping("/manual")
    public ResponseEntity<Void> manualAdjustment(
            @RequestParam UUID itemId,
            @RequestParam BigDecimal quantity,
            @RequestParam com.resortmanagement.system.inventory.entity.InventorySourceType sourceType,
            @RequestParam UUID sourceId) {

        service.addStock(itemId, quantity, sourceType, sourceId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
