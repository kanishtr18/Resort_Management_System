package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.RoomBlockCreateRequest;
import com.resortmanagement.system.room.dto.request.RoomBlockUpdateRequest;
import com.resortmanagement.system.room.dto.response.RoomBlockResponse;
import com.resortmanagement.system.room.service.RoomBlockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/room-blocks")
public class RoomBlockController {

    private final RoomBlockService service;

    public RoomBlockController(RoomBlockService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'maintenance:view', 'maintenance:manage', 'ADMIN')")
    public ResponseEntity<List<RoomBlockResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'maintenance:view', 'maintenance:manage', 'ADMIN')")
    public ResponseEntity<RoomBlockResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('rooms:manage', 'maintenance:manage', 'ADMIN')")
    public ResponseEntity<RoomBlockResponse> create(
            @Valid @RequestBody RoomBlockCreateRequest dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rooms:manage', 'maintenance:manage', 'ADMIN')")
    public ResponseEntity<RoomBlockResponse> update(
            @PathVariable UUID id,
            @RequestBody RoomBlockUpdateRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rooms:manage', 'maintenance:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}