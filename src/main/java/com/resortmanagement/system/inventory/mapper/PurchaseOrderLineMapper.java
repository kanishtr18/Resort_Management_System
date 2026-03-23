package com.resortmanagement.system.inventory.mapper;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.inventory.dto.response.PurchaseOrderLineResponse;
import com.resortmanagement.system.inventory.entity.PurchaseOrderLine;

@Component
public class PurchaseOrderLineMapper {

    public PurchaseOrderLineResponse toResponse(PurchaseOrderLine entity) {
        if (entity == null) return null;

        PurchaseOrderLineResponse res = new PurchaseOrderLineResponse();
        res.setId(entity.getId());
        res.setQty(entity.getQty());
        res.setUnitPrice(entity.getUnitPrice());
        res.setTotalPrice(entity.getTotalPrice());

        if (entity.getInventoryItem() != null) {
            res.setInventoryItemId(entity.getInventoryItem().getId());
            res.setInventoryItemName(entity.getInventoryItem().getName());
        }
        return res;
    }
}