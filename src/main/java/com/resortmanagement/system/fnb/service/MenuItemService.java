package com.resortmanagement.system.fnb.service;

import com.resortmanagement.system.fnb.entity.MenuItem;
import com.resortmanagement.system.fnb.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.resortmanagement.system.fnb.dto.response.MenuItemResponse;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MenuItemService {

    private final MenuItemRepository repository;
    private final com.resortmanagement.system.fnb.repository.MenuRepository menuRepository;
    private final com.resortmanagement.system.inventory.repository.InventoryItemRepository inventoryItemRepository;
    private final com.resortmanagement.system.fnb.mapper.MenuItemMapper mapper;

    public MenuItemService(
            MenuItemRepository repository,
            com.resortmanagement.system.fnb.repository.MenuRepository menuRepository,
            com.resortmanagement.system.inventory.repository.InventoryItemRepository inventoryItemRepository,
            com.resortmanagement.system.fnb.mapper.MenuItemMapper mapper) {
        this.repository = repository;
        this.menuRepository = menuRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.mapper = mapper;
    }

    /**
     * Fetch only active (not soft-deleted) menu items
     */
    public List<MenuItemResponse> findAllActive() {
        return repository.findAllActive().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<MenuItemResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public Optional<MenuItemResponse> findById(UUID id) {
        return repository.findByIdAndDeletedFalse(id).map(mapper::toResponse);
    }

    public MenuItem findMenuItem(UUID id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + id));
    }

    @Transactional
    public MenuItemResponse create(com.resortmanagement.system.fnb.dto.request.MenuItemRequest request) {
        MenuItem entity = mapper.toEntity(request);
        
        if (request.getMenuId() != null) {
            com.resortmanagement.system.fnb.entity.Menu menu = menuRepository.findById(request.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found"));
            entity.setMenu(menu);
        }

        // Handle ingredients if sent in request
        if (request.getIngredients() != null) {
            for (com.resortmanagement.system.fnb.dto.request.MenuItemIngredientRequest ingRequest : request.getIngredients()) {
                 com.resortmanagement.system.fnb.entity.MenuItemIngredient ingredient = new com.resortmanagement.system.fnb.entity.MenuItemIngredient();
                 ingredient.setMenuItem(entity);
                 ingredient.setQuantityRequired(ingRequest.getQuantityRequired());
                 ingredient.setUnit(ingRequest.getUnit());
                 
                 com.resortmanagement.system.inventory.entity.InventoryItem invItem = inventoryItemRepository.findById(ingRequest.getInventoryItemId())
                         .orElseThrow(() -> new RuntimeException("Inventory item not found: " + ingRequest.getInventoryItemId()));
                 ingredient.setInventoryItem(invItem);
                 
                 entity.getIngredients().add(ingredient);
            }
        }

        return mapper.toResponse(repository.save(entity));
    }

    public com.resortmanagement.system.fnb.dto.response.MenuItemResponse update(UUID id, com.resortmanagement.system.fnb.dto.request.MenuItemRequest request) {
        MenuItem entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + id));
        
        mapper.updateEntity(entity, request);
        
        if (request.getMenuId() != null) {
            com.resortmanagement.system.fnb.entity.Menu menu = menuRepository.findById(request.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found"));
            entity.setMenu(menu);
        }
        
        // Re-saving ingredients logic is complex (merge/replace). For now, simplest is clear and re-add if provided, 
        // or separate endpoint for ingredients.
        // Assuming update replaces ingredients if provided
        if (request.getIngredients() != null) {
            entity.getIngredients().clear();
            for (com.resortmanagement.system.fnb.dto.request.MenuItemIngredientRequest ingRequest : request.getIngredients()) {
                 com.resortmanagement.system.fnb.entity.MenuItemIngredient ingredient = new com.resortmanagement.system.fnb.entity.MenuItemIngredient();
                 ingredient.setMenuItem(entity);
                 ingredient.setQuantityRequired(ingRequest.getQuantityRequired());
                 ingredient.setUnit(ingRequest.getUnit());
                 
                 com.resortmanagement.system.inventory.entity.InventoryItem invItem = inventoryItemRepository.findById(ingRequest.getInventoryItemId())
                         .orElseThrow(() -> new RuntimeException("Inventory item not found: " + ingRequest.getInventoryItemId()));
                 ingredient.setInventoryItem(invItem);
                 
                 entity.getIngredients().add(ingredient);
            }
        }

        return mapper.toResponse(repository.save(entity));
    }

    /**
     * Soft delete menu item (do NOT hard delete)
     */
    @Transactional
    public void delete(UUID id) {
        repository.softDeleteById(id, Instant.now());
    }
}
