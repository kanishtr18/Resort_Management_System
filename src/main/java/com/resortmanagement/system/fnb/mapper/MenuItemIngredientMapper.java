package com.resortmanagement.system.fnb.mapper;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.fnb.dto.response.MenuItemIngredientResponse;
import com.resortmanagement.system.fnb.entity.MenuItemIngredient;

@Component
public class MenuItemIngredientMapper {

    public MenuItemIngredientResponse toResponse(MenuItemIngredient entity) {
        if (entity == null) return null;

        MenuItemIngredientResponse res = new MenuItemIngredientResponse();
        res.setId(entity.getId());
        res.setQuantityRequired(entity.getQuantityRequired());
        res.setUnit(entity.getUnit());

        if (entity.getMenuItem() != null) {
            res.setMenuItemId(entity.getMenuItem().getId());
            res.setMenuItemName(entity.getMenuItem().getName());
        }
        if (entity.getInventoryItem() != null) {
            res.setInventoryItemId(entity.getInventoryItem().getId());
            res.setInventoryItemName(entity.getInventoryItem().getName());
        }
        return res;
    }
}