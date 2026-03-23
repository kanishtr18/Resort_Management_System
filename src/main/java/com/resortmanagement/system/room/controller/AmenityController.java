package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.AmenityCreateRequest;
import com.resortmanagement.system.room.dto.request.AmenityUpdateRequest;
import com.resortmanagement.system.room.dto.response.AmenityResponse;
import com.resortmanagement.system.room.service.AmenityService;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService service;

    public AmenityController(AmenityService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'ADMIN')")
    public ResponseEntity<List<AmenityResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'ADMIN')")
    public ResponseEntity<AmenityResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // Admin only
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AmenityResponse> create(
            @RequestBody AmenityCreateRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AmenityResponse> update(
            @PathVariable UUID id,
            @RequestBody AmenityUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}