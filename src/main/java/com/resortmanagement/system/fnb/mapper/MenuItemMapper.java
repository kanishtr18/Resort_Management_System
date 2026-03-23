package com.resortmanagement.system.fnb.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.fnb.dto.request.MenuItemRequest;
import com.resortmanagement.system.fnb.dto.response.MenuItemIngredientResponse;
import com.resortmanagement.system.fnb.dto.response.MenuItemResponse;
import com.resortmanagement.system.fnb.entity.MenuItem;
import com.resortmanagement.system.fnb.entity.MenuItemIngredient;

@Component
public class MenuItemMapper {

    public MenuItem toEntity(MenuItemRequest request) {
        if (request == null) {
            return null;
        }
        MenuItem entity = new MenuItem();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setAvailable(request.isAvailable());
        // Menu relationship is handled in service
        return entity;
    }

    public MenuItemResponse toResponse(MenuItem entity) {
        if (entity == null) {
            return null;
        }
        MenuItemResponse response = new MenuItemResponse();
        response.setId(entity.getId());
        response.setMenuId(entity.getMenu() != null ? entity.getMenu().getId() : null);
        response.setMenuName(entity.getMenu() != null ? entity.getMenu().getName() : null);
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setAvailable(entity.isAvailable());

        if (entity.getIngredients() != null) {
            response.setIngredients(entity.getIngredients().stream()
                .map(this::toIngredientResponse)
                .collect(Collectors.toList()));
        }

        return response;
    }

    private MenuItemIngredientResponse toIngredientResponse(MenuItemIngredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        MenuItemIngredientResponse response = new MenuItemIngredientResponse();
        response.setId(ingredient.getId());
        response.setInventoryItemId(
            ingredient.getInventoryItem() != null ? ingredient.getInventoryItem().getId() : null);
        response.setInventoryItemName(
            ingredient.getInventoryItem() != null ? ingredient.getInventoryItem().getName() : null);
        response.setQuantityRequired(ingredient.getQuantityRequired());
        response.setUnit(ingredient.getUnit());
        return response;
    }

    public void updateEntity(MenuItem entity, MenuItemRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setAvailable(request.isAvailable());
    }
}
