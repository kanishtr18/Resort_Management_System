package com.resortmanagement.system.reporting.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.reporting.dto.AuditLogResponse;
import com.resortmanagement.system.reporting.mapper.ReportingMapper;
import com.resortmanagement.system.reporting.service.AuditLogService;
import java.util.stream.Collectors;

/**
 * AuditLogController
 * Purpose:
 *  - REST controller for AuditLog read-only operations
 * Endpoints:
 *  - GET /api/reporting/audit-logs - Get all audit logs
 *  - GET /api/reporting/audit-logs/{id} - Get audit log by ID
 *  - GET /api/reporting/audit-logs/entity/{entityName} - Get audit logs for specific entity type
 *  - GET /api/reporting/audit-logs/target/{targetId} - Get audit logs for specific entity instance
 * Note:
 *  - Audit logs are read-only (no POST/PUT/DELETE)
 *  - Creation happens automatically in service layers of other domains
 */
@RestController
@RequestMapping("/api/reporting/audit-logs")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(ReportingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ReportingMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entity/{entityName}")
    public ResponseEntity<List<AuditLogResponse>> getByEntity(@PathVariable String entityName) {
        return ResponseEntity.ok(service.findByTargetEntity(entityName).stream()
                .map(ReportingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<AuditLogResponse>> getByTargetId(@PathVariable UUID targetId) {
        return ResponseEntity.ok(service.findByTargetId(targetId).stream()
                .map(ReportingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/user/{performedBy}")
    public ResponseEntity<List<AuditLogResponse>> getByPerformedBy(@PathVariable String performedBy) {
        return ResponseEntity.ok(service.findByPerformedBy(performedBy).stream()
                .map(ReportingMapper::toResponse)
                .collect(Collectors.toList()));
    }
}
