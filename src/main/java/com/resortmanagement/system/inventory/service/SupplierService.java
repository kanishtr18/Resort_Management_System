package com.resortmanagement.system.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.inventory.entity.Supplier;
import com.resortmanagement.system.inventory.repository.SupplierRepository;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository repository;
    private final com.resortmanagement.system.inventory.mapper.SupplierMapper mapper;

    public SupplierService(SupplierRepository repository, com.resortmanagement.system.inventory.mapper.SupplierMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<com.resortmanagement.system.inventory.dto.response.SupplierResponse> findAllActive() {
        return repository.findByDeletedFalse().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<com.resortmanagement.system.inventory.dto.response.SupplierResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public Optional<com.resortmanagement.system.inventory.dto.response.SupplierResponse> findById(UUID id) {
        return repository.findById(id)
                .filter(supplier -> !supplier.isDeleted())
                .map(mapper::toResponse);
    }
    @Transactional
    public com.resortmanagement.system.inventory.dto.response.SupplierResponse create(com.resortmanagement.system.inventory.dto.request.SupplierRequest request) {
        Supplier supplier = mapper.toEntity(request);
        return mapper.toResponse(repository.save(supplier));
    }
    @Transactional
    public com.resortmanagement.system.inventory.dto.response.SupplierResponse update(UUID id, com.resortmanagement.system.inventory.dto.request.SupplierRequest request) {
        Supplier supplier = repository.findById(id)
                .filter(s -> !s.isDeleted())
                .orElseThrow(() -> new RuntimeException("Supplier not found or deleted: " + id));
        mapper.updateEntity(supplier, request);
        return mapper.toResponse(repository.save(supplier));
    }

    /**
     * Soft delete supplier
     */
    @Transactional
    public void delete(UUID id) {
        repository.softDeleteById(id, java.time.Instant.now());
    }
}

