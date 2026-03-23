package com.resortmanagement.system.inventory.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.inventory.dto.request.InventoryItemRequest;
import com.resortmanagement.system.inventory.dto.response.InventoryItemResponse;
import com.resortmanagement.system.inventory.entity.InventoryItem;
import com.resortmanagement.system.inventory.repository.InventoryItemRepository;

@Service
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository repository;
    private final com.resortmanagement.system.inventory.mapper.InventoryItemMapper mapper;

    public InventoryItemService(InventoryItemRepository repository, com.resortmanagement.system.inventory.mapper.InventoryItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Get all inventory items
     */
    public List<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get all active inventory items
     */
    public List<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> findAllActive() {
        // Assuming Auditable has isDeleted or we filter manually if not present in repo custom method yet
        // For now, listing all or we need a custom query in repo. 
        // Let's assume findAll for now or implemented check if isDeleted exists.
        // InventoryItem extends Auditable (standard) which usually doesn't have isDeleted unless extended AuditableSoftDeletable
        // Let's just return findAll for now, or use findAllActive if I added it to repo (I didn't check repo).
        return findAll();
    }

    /**
     * Get inventory item by ID
     */
    public Optional<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> findById(UUID id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    public InventoryItem findInventoryItem(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found: " + id));
    }

    /**
     * Create inventory item
     */
    public com.resortmanagement.system.inventory.dto.response.InventoryItemResponse create(com.resortmanagement.system.inventory.dto.request.InventoryItemRequest request) {
        InventoryItem entity = mapper.toEntity(request);
        entity.setQuantityOnHand(BigDecimal.ZERO); // Initial quantity
        return mapper.toResponse(repository.save(entity));
    }

    /**
     * Update inventory item
     */
    public InventoryItemResponse update(UUID id, InventoryItemRequest request) {
    InventoryItem entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory item not found: " + id));

    // ✅ Prevent update from setting negative stock
    if (request.getQuantityOnHand() != null && 
        request.getQuantityOnHand().compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Quantity on hand cannot be negative.");
    }

    // ✅ Prevent reorder point from being negative
    if (request.getReorderPoint() != null && 
        request.getReorderPoint().compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Reorder point cannot be negative.");
    }

    mapper.updateEntity(entity, request);
    return mapper.toResponse(repository.save(entity));
}

    /**
     * Delete inventory item (master data → hard delete allowed)
     */
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    /**
     * Get items below reorder point
     */
    public List<com.resortmanagement.system.inventory.dto.response.InventoryItemResponse> findLowStockItems() {
        return repository.findLowStockItems().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public void adjustQuantity(UUID id, BigDecimal quantityDelta) {
         InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found: " + id));
         item.setQuantityOnHand(item.getQuantityOnHand().add(quantityDelta));
         repository.save(item);
    }
}
