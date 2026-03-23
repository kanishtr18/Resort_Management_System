package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.MaintenanceRequestCreateRequest;
import com.resortmanagement.system.room.dto.response.MaintenanceRequestResponse;
import com.resortmanagement.system.room.service.MaintenanceRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceRequestController {

    private final MaintenanceRequestService service;

    public MaintenanceRequestController(MaintenanceRequestService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('maintenance:view', 'maintenance:manage', 'ADMIN')")
    public ResponseEntity<List<MaintenanceRequestResponse>> getAll() {
        return ResponseEntity.ok(service.getAllOpen());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('maintenance:manage', 'ADMIN')")
    public ResponseEntity<MaintenanceRequestResponse> create(
            @Valid @RequestBody MaintenanceRequestCreateRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('maintenance:manage', 'ADMIN')")
    public ResponseEntity<Void> close(@PathVariable UUID id) {
        service.close(id);
        return ResponseEntity.noContent().build();
    }
}