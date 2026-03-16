package com.resortmanagement.system.inventory.dto.request;

import java.math.BigDecimal;
import java.util.UUID;
 
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// import lombok.Data;

public class PurchaseOrderLineRequest {

    @NotNull(message = "Inventory item ID is required")
    private UUID inventoryItemId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal qty;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;

    // Manual Getters and Setters

    public UUID getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(UUID inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

