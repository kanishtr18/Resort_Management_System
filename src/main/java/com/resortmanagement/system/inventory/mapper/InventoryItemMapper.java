package com.resortmanagement.system.inventory.mapper;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.inventory.dto.request.InventoryItemRequest;
import com.resortmanagement.system.inventory.dto.response.InventoryItemResponse;
import com.resortmanagement.system.inventory.entity.InventoryItem;

@Component
public class InventoryItemMapper {

    public InventoryItem toEntity(InventoryItemRequest request) {
        if (request == null) {
            return null;
        }
        InventoryItem entity = new InventoryItem();
        entity.setSku(request.getSku());
        entity.setName(request.getName());
        entity.setBaseUnit(request.getBaseUnit());
        entity.setReorderPoint(request.getReorderPoint());
        entity.setUnitCost(request.getUnitCost());
        // quantityOnHand is initialized to 0 or handled by business logic
        return entity;
    }

    public InventoryItemResponse toResponse(InventoryItem entity) {
        if (entity == null) {
            return null;
        }
        InventoryItemResponse response = new InventoryItemResponse();
        response.setId(entity.getId());
        response.setSku(entity.getSku());
        response.setName(entity.getName());
        response.setBaseUnit(entity.getBaseUnit());
        response.setQuantityOnHand(entity.getQuantityOnHand());
        response.setReorderPoint(entity.getReorderPoint());
        response.setUnitCost(entity.getUnitCost());
        return response;
    }

    public void updateEntity(InventoryItem entity, InventoryItemRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setName(request.getName());
        entity.setReorderPoint(request.getReorderPoint());
        entity.setUnitCost(request.getUnitCost());

        if (request.getQuantityOnHand() != null) {
        entity.setQuantityOnHand(request.getQuantityOnHand());
    }

    // ✅ Only update reorderQty if explicitly provided
    if (request.getReorderQty() != null) {
        entity.setReorderQty(request.getReorderQty());
    }
        // Sku/BaseUnit typically not changeable after creation due to hist/ref integ
    }
}
