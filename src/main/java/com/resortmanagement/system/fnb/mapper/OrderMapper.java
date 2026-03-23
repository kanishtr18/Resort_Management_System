package com.resortmanagement.system.fnb.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.fnb.dto.request.OrderRequest;
import com.resortmanagement.system.fnb.dto.response.OrderItemResponse;
import com.resortmanagement.system.fnb.dto.response.OrderResponse;
import com.resortmanagement.system.fnb.entity.Order;
import com.resortmanagement.system.fnb.entity.OrderItem;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequest request) {
        if (request == null) {
            return null;
        }
        Order entity = new Order();
        entity.setTableId(request.getTableId());
        // Items are typically handled by service logic during creation 
        // as they require fetching MenuItems
        return entity;
    }

    public OrderResponse toResponse(Order entity) {
    if (entity == null) return null;

    OrderResponse response = new OrderResponse();
    response.setId(entity.getId());
    response.setTableId(entity.getTableId());
    response.setTotalAmount(entity.getTotalAmount());
    response.setPlacedAt(entity.getPlacedAt());

    // Fix: map guest and reservation (renamed fields in entity)
    response.setGuestId(entity.getGuest() != null ? entity.getGuest().getId() : null);
    response.setReservationId(entity.getReservation() != null ? entity.getReservation().getId() : null);

    response.setAssignedFolioId(entity.getAssignedFolio() != null ? entity.getAssignedFolio().getId() : null);

    if (entity.getStatus() != null) {
        response.setStatus(entity.getStatus().name());
    }

    response.setItems(entity.getOrderItems() != null
        ? entity.getOrderItems().stream().map(this::toItemResponse).collect(Collectors.toList())
        : Collections.emptyList());

    return response;
}

    private OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) {
            return null;
        }
        OrderItemResponse res = new OrderItemResponse();
        res.setId(item.getId());
        if (item.getMenuItem() != null) {
            res.setMenuItemId(item.getMenuItem().getId());
            res.setMenuItemName(item.getMenuItem().getName());
        }
        res.setQty(item.getQuantity());
        res.setUnitPrice(item.getUnitPrice());
        res.setTotalPrice(item.getTotalPrice());
        return res;
    }
}
