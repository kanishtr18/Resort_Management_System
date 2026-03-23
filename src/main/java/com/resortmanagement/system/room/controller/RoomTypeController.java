package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.RoomTypeCreateRequest;
import com.resortmanagement.system.room.dto.request.RoomTypeUpdateRequest;
import com.resortmanagement.system.room.dto.response.RoomTypeResponse;
import com.resortmanagement.system.room.service.RoomTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService service;

    public RoomTypeController(RoomTypeService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'reservations:view', 'reservations:create', 'ADMIN')")
    public ResponseEntity<List<RoomTypeResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'reservations:view', 'reservations:create', 'ADMIN')")
    public ResponseEntity<RoomTypeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // Admin only
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomTypeResponse> create(
            @Valid @RequestBody RoomTypeCreateRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomTypeResponse> update(
            @PathVariable UUID id,
            @RequestBody RoomTypeUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}