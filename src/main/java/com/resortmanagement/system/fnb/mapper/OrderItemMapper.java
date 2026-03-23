package com.resortmanagement.system.fnb.mapper;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.fnb.dto.response.OrderItemResponse;
import com.resortmanagement.system.fnb.entity.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemResponse toResponse(OrderItem item) {
        if (item == null) return null;

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