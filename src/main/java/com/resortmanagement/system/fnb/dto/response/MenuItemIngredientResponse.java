package com.resortmanagement.system.fnb.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItemIngredientResponse {

    private UUID id;
    private UUID menuItemId;
    private String menuItemName;
    private UUID inventoryItemId;
    private String inventoryItemName;
    private BigDecimal quantityRequired;
    private String unit;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getMenuItemId() { return menuItemId; }
    public void setMenuItemId(UUID menuItemId) { this.menuItemId = menuItemId; }

    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }

    public UUID getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(UUID inventoryItemId) { this.inventoryItemId = inventoryItemId; }

    public String getInventoryItemName() { return inventoryItemName; }
    public void setInventoryItemName(String inventoryItemName) { this.inventoryItemName = inventoryItemName; }

    public BigDecimal getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(BigDecimal quantityRequired) { this.quantityRequired = quantityRequired; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}