package com.resortmanagement.system.reporting.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.reporting.entity.ReportMeta;
import com.resortmanagement.system.reporting.repository.ReportMetaRepository;

/**
 * ReportMetaService
 * Purpose:
 *  - Service layer for ReportMeta entity operations
 *  - Handles report metadata operations (no deletion - immutable records)
 * Business Logic:
 *  - Validates report metadata before saving
 *  - Report metadata is immutable once created
 */
@Service
@Transactional
public class ReportMetaService {

    private final ReportMetaRepository repository;

    public ReportMetaService(ReportMetaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ReportMeta> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ReportMeta> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ReportMeta> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<ReportMeta> findByOwnerId(UUID ownerId ) {
        return repository.findByOwnerId(ownerId);
    }

    public ReportMeta save(ReportMeta reportMeta) {
        // Validation: ensure required fields are present
        if (reportMeta.getName() == null || reportMeta.getName().trim().isEmpty()) {
            throw new ApplicationException("Report name is required");
        }
        
        // Check for duplicate report name
        if (reportMeta.getId() == null) {
            Optional<ReportMeta> existing = repository.findByName(reportMeta.getName());
            if (existing.isPresent()) {
                throw new ApplicationException("Report with name already exists: " + reportMeta.getName());
            }
        }
        
        return repository.save(reportMeta);
    }
}
