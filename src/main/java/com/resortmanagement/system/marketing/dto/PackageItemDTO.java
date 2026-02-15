package com.resortmanagement.system.marketing.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageItemDTO {
    private UUID id;

    // Package
    private UUID pkgId;
    private String pkgName;

    // Optional inclusions (ONLY ONE should be non-null)
    private UUID roomTypeId;
    private String roomTypeName;

    private UUID serviceItemId;
    private String serviceItemName;

    private UUID menuItemId;
    private String menuItemName;

    // Inventory
    private UUID inventoryItemId;
    private String inventoryItemName;
    private BigDecimal qty; // quantity required per package

    private BigDecimal price; // optional override price
}
