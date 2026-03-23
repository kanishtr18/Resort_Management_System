package com.resortmanagement.system.inventory.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.inventory.entity.InventoryItem;
import com.resortmanagement.system.inventory.entity.InventorySourceType;
import com.resortmanagement.system.inventory.entity.InventoryTransaction;
import com.resortmanagement.system.inventory.repository.InventoryItemRepository;
import com.resortmanagement.system.inventory.repository.InventoryTransactionRepository;

@Transactional
@Service
public class InventoryTransactionService {

    private final InventoryItemRepository itemRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final com.resortmanagement.system.inventory.mapper.InventoryTransactionMapper mapper;

    public InventoryTransactionService(
            InventoryItemRepository itemRepository,
            InventoryTransactionRepository transactionRepository,
            com.resortmanagement.system.inventory.mapper.InventoryTransactionMapper mapper) {
        this.itemRepository = itemRepository;
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    /**
     * Consume ingredients atomically (used for Orders)
     */
    @Transactional
    public void consumeIngredients(
            Map<UUID, BigDecimal> items,
            com.resortmanagement.system.inventory.entity.InventorySourceType sourceType,
            UUID sourceId) {

        for (Map.Entry<UUID, BigDecimal> entry : items.entrySet()) {

            UUID itemId = entry.getKey();
            BigDecimal qtyRequired = entry.getValue();

            int updated = itemRepository.consumeStock(itemId, qtyRequired);
            if (updated == 0) {
                throw new RuntimeException("Insufficient inventory for item: " + itemId);
            }

            InventoryItem item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Inventory item not found: " + itemId));

            InventoryTransaction tx = new InventoryTransaction();
            tx.setItem(item);
            tx.setQtyChange(qtyRequired.negate());
            tx.setSourceType(sourceType);
            tx.setSourceId(sourceId);

            transactionRepository.save(tx);
        }
    }

    /**
     * Add stock (Purchase Order / Adjustment)
     */
    @Transactional
public void addStock(UUID itemId, BigDecimal qtyAdded, InventorySourceType sourceType, UUID sourceId) {
    InventoryItem item = itemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Inventory item not found: " + itemId));

    // Check if current quantity is null before adding
    BigDecimal currentQty = item.getQuantityOnHand() != null ? item.getQuantityOnHand() : BigDecimal.ZERO;
    item.setQuantityOnHand(currentQty.add(qtyAdded));
    
    itemRepository.save(item);

    InventoryTransaction tx = new InventoryTransaction();
    tx.setItem(item);
    tx.setQtyChange(qtyAdded);
    tx.setSourceType(sourceType);
    tx.setSourceId(sourceId);
    tx.setTransactionDate(java.time.LocalDateTime.now()); // Set date explicitly

    transactionRepository.save(tx);
}

    public java.util.List<com.resortmanagement.system.inventory.dto.response.InventoryTransactionResponse> findAll() {
        return transactionRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
