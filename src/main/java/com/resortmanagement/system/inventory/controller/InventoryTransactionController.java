// package com.resortmanagement.system.inventory.controller;

// import java.util.List;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.resortmanagement.system.inventory.dto.request.InventoryTransactionRequest;
// import com.resortmanagement.system.inventory.service.InventoryTransactionService;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/inventory/transactions")
// public class InventoryTransactionController {

//     private final InventoryTransactionService service;

//     public InventoryTransactionController(InventoryTransactionService service) {
//         this.service = service;
//     }

//     /**
//      * View inventory transactions
//      * Optional filter: itemId (Not implemented in service yet but kept for
//      * structure)
//      */
//     @GetMapping
//     public ResponseEntity<List<com.resortmanagement.system.inventory.dto.response.InventoryTransactionResponse>> getAll() {
//         // Ignoring itemId filter for now as service refactor didn't include it fully or
//         // I missed it.
//         // Returning findAll
//         return ResponseEntity.ok(service.findAll());
//     }

//     /**
//      * Manual inventory adjustment (ADMIN only)
//      */
//     // @PostMapping("/manual")
//     // public ResponseEntity<Void> manualAdjustment(
//     //         @RequestParam UUID itemId,
//     //         @RequestParam BigDecimal quantity,
//     //         @RequestParam com.resortmanagement.system.inventory.entity.InventorySourceType sourceType,
//     //         @RequestParam UUID sourceId) {

//     //     service.addStock(itemId, quantity, sourceType, sourceId);
//     //     return ResponseEntity.status(HttpStatus.CREATED).build();
//     // }
//     @PostMapping("/manual")
// public ResponseEntity<Void> manualAdjustment(
//         @Valid @RequestBody InventoryTransactionRequest request) { // Use @RequestBody

//     // Pass values from the request DTO to the service
//     service.addStock(
//         request.getInventoryItemId(), 
//         request.getQtyChange(), 
//         request.getSourceType(), 
//         request.getSourceId()
//     );
    
//     return ResponseEntity.status(HttpStatus.CREATED).build();
// }
// }

package com.resortmanagement.system.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.inventory.dto.request.InventoryTransactionRequest;
import com.resortmanagement.system.inventory.dto.response.InventoryTransactionResponse;
import com.resortmanagement.system.inventory.service.InventoryTransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/transactions")
public class InventoryTransactionController {

    private final InventoryTransactionService service;

    public InventoryTransactionController(InventoryTransactionService service) {
        this.service = service;
    }

    // view only — both inventory:view and inventory:manage can see transactions
    @GetMapping
    @PreAuthorize("hasAnyAuthority('inventory:view', 'inventory:manage', 'ADMIN')")
    public ResponseEntity<List<InventoryTransactionResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // manual stock adjustment — manage only
    @PostMapping("/manual")
    @PreAuthorize("hasAnyAuthority('inventory:manage', 'ADMIN')")
    public ResponseEntity<Void> manualAdjustment(
            @Valid @RequestBody InventoryTransactionRequest request) {
        service.addStock(
            request.getInventoryItemId(),
            request.getQtyChange(),
            request.getSourceType(),
            request.getSourceId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}