package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.RoomCreateRequest;
import com.resortmanagement.system.room.dto.request.RoomUpdateRequest;
import com.resortmanagement.system.room.dto.response.RoomResponse;
import com.resortmanagement.system.room.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'housekeeping:view', 'housekeeping:manage', 'maintenance:view', 'maintenance:manage', 'reservations:view', 'ADMIN')")
    public ResponseEntity<List<RoomResponse>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'housekeeping:view', 'housekeeping:manage', 'maintenance:view', 'maintenance:manage', 'reservations:view', 'ADMIN')")
    public ResponseEntity<RoomResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    // Admin only — employees never create rooms
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponse> create(
            @Valid @RequestBody RoomCreateRequest request) {
        return new ResponseEntity<>(roomService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RoomUpdateRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}