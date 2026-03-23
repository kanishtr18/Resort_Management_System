package com.resortmanagement.system.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.inventory.dto.response.PurchaseOrderLineResponse;
import com.resortmanagement.system.inventory.entity.PurchaseOrderLine;
import com.resortmanagement.system.inventory.mapper.PurchaseOrderLineMapper;
import com.resortmanagement.system.inventory.repository.PurchaseOrderLineRepository;

@Service
public class PurchaseOrderLineService {

    private final PurchaseOrderLineRepository repository;
    private final PurchaseOrderLineMapper mapper;

    public PurchaseOrderLineService(
            PurchaseOrderLineRepository repository,
            PurchaseOrderLineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PurchaseOrderLineResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<PurchaseOrderLineResponse> findById(UUID id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    // Fix: was throwing UnsupportedOperationException
    public List<PurchaseOrderLineResponse> findByPurchaseOrderId(UUID purchaseOrderId) {
        return repository.findByPurchaseOrderId(purchaseOrderId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}