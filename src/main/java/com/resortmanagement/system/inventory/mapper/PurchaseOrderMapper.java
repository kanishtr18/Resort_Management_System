package com.resortmanagement.system.inventory.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.inventory.dto.request.PurchaseOrderRequest;
import com.resortmanagement.system.inventory.dto.response.PurchaseOrderLineResponse;
import com.resortmanagement.system.inventory.dto.response.PurchaseOrderResponse;
import com.resortmanagement.system.inventory.entity.PurchaseOrder;
import com.resortmanagement.system.inventory.entity.PurchaseOrderLine;
import com.resortmanagement.system.inventory.entity.Supplier;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrder toEntity(PurchaseOrderRequest request, Supplier supplier) {
        if (request == null) {
            return null;
        }
        PurchaseOrder entity = new PurchaseOrder();
        entity.setSupplier(supplier);
        entity.setExpectedDelivery(request.getExpectedDelivery());
        // Status, totalAmount, lines are handled by service logic
        return entity;
    }

    public PurchaseOrderResponse toResponse(PurchaseOrder entity, Supplier supplier, java.util.List<PurchaseOrderLine> lines) {
        if (entity == null) {
            return null;
        }
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        response.setId(entity.getId());
        response.setPoNumber(entity.getPoNumber());
        response.setSupplierId(supplier.getId());
        response.setSupplierName(supplier.getName());
        response.setStatus(entity.getStatus());
        response.setExpectedDelivery(entity.getExpectedDelivery());
        response.setTotalAmount(entity.getTotalAmount());
        
        if (lines != null) {
            response.setLines(lines.stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList()));
        }
        return response;
    }

    private PurchaseOrderLineResponse toLineResponse(PurchaseOrderLine line) {
        if (line == null) {
            return null;
        }
        PurchaseOrderLineResponse response = new PurchaseOrderLineResponse();
        response.setId(line.getId());
        response.setInventoryItemId(line.getInventoryItem().getId());
        response.setInventoryItemName(line.getInventoryItem().getName());
        response.setQty(line.getQty());
        response.setUnitPrice(line.getUnitPrice());
        response.setTotalPrice(line.getTotalPrice());
        return response;
    }
}
