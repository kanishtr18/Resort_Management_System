package com.resortmanagement.system.fnb.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MenuItemIngredientRequest {

    @NotNull(message = "Menu item ID is required")
    private UUID menuItemId;

    @NotNull(message = "Inventory item ID is required")
    private UUID inventoryItemId;

    @NotNull(message = "Quantity required is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantityRequired;

    @NotBlank(message = "Unit is required")
    private String unit;

    public UUID getMenuItemId() { return menuItemId; }
    public void setMenuItemId(UUID menuItemId) { this.menuItemId = menuItemId; }

    public UUID getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(UUID inventoryItemId) { this.inventoryItemId = inventoryItemId; }

    public BigDecimal getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(BigDecimal quantityRequired) { this.quantityRequired = quantityRequired; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}