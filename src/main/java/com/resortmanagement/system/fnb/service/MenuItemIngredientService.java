package com.resortmanagement.system.fnb.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.fnb.dto.request.MenuItemIngredientRequest;
import com.resortmanagement.system.fnb.dto.response.MenuItemIngredientResponse;
import com.resortmanagement.system.fnb.entity.MenuItemIngredient;
import com.resortmanagement.system.fnb.mapper.MenuItemIngredientMapper;
import com.resortmanagement.system.fnb.repository.MenuItemIngredientRepository;
import com.resortmanagement.system.inventory.repository.InventoryItemRepository;
import com.resortmanagement.system.fnb.repository.MenuItemRepository;

@Service
public class MenuItemIngredientService {

    private final MenuItemIngredientRepository repository;
    private final MenuItemIngredientMapper mapper;
    private final MenuItemRepository menuItemRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public MenuItemIngredientService(
            MenuItemIngredientRepository repository,
            MenuItemIngredientMapper mapper,
            MenuItemRepository menuItemRepository,
            InventoryItemRepository inventoryItemRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.menuItemRepository = menuItemRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<MenuItemIngredientResponse> findAll() {
        return repository.findAll().stream()
                .filter(i -> i.getMenuItem() != null && !i.getMenuItem().isDeleted())
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<MenuItemIngredientResponse> findById(UUID id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    public List<MenuItemIngredientResponse> findByMenuItem(UUID menuItemId) {
        return repository.findByMenuItemId(menuItemId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public MenuItemIngredientResponse create(MenuItemIngredientRequest request) {
        MenuItemIngredient entity = new MenuItemIngredient();

        entity.setMenuItem(
            menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + request.getMenuItemId()))
        );
        entity.setInventoryItem(
            inventoryItemRepository.findById(request.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("InventoryItem not found: " + request.getInventoryItemId()))
        );
        entity.setQuantityRequired(request.getQuantityRequired());
        entity.setUnit(request.getUnit());

        return mapper.toResponse(repository.save(entity));
    }

    public MenuItemIngredientResponse update(UUID id, MenuItemIngredientRequest request) {
        MenuItemIngredient entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItemIngredient not found: " + id));

        entity.setMenuItem(
            menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + request.getMenuItemId()))
        );
        entity.setInventoryItem(
            inventoryItemRepository.findById(request.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("InventoryItem not found: " + request.getInventoryItemId()))
        );
        entity.setQuantityRequired(request.getQuantityRequired());
        entity.setUnit(request.getUnit());

        return mapper.toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}