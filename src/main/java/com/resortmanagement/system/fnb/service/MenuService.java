package com.resortmanagement.system.fnb.service;

import com.resortmanagement.system.fnb.dto.response.MenuResponse;
import com.resortmanagement.system.fnb.entity.Menu;
import com.resortmanagement.system.fnb.repository.MenuRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository repository;
    private final com.resortmanagement.system.fnb.mapper.MenuMapper mapper;

    public MenuService(MenuRepository repository, com.resortmanagement.system.fnb.mapper.MenuMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Fetch only active (not soft-deleted) menus
    public List<com.resortmanagement.system.fnb.dto.response.MenuResponse> findAllActive() {
        return repository.findByDeletedFalse().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<MenuResponse> findAll() {
    return repository.findByDeletedFalse().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public Optional<com.resortmanagement.system.fnb.dto.response.MenuResponse> findById(UUID id) {
        return repository.findById(id)
                .filter(menu -> !menu.isDeleted())
                .map(mapper::toResponse);
    }

    public com.resortmanagement.system.fnb.dto.response.MenuResponse create(com.resortmanagement.system.fnb.dto.request.MenuRequest request) {
        Menu menu = mapper.toEntity(request);
        return mapper.toResponse(repository.save(menu));
    }

    public com.resortmanagement.system.fnb.dto.response.MenuResponse update(UUID id, com.resortmanagement.system.fnb.dto.request.MenuRequest request) {
        Menu menu = repository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Menu not found or deleted: " + id));
        mapper.updateEntity(menu, request);
        return mapper.toResponse(repository.save(menu));
    }

    // Soft delete menu
    @Transactional
    public void delete(UUID id) {
        repository.softDeleteById(id, Instant.now());
    }
}
