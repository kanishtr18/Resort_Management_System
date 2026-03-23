package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.RoomAmenityCreateRequest;
import com.resortmanagement.system.room.dto.response.RoomAmenityResponse;
import com.resortmanagement.system.room.service.RoomAmenityService;

@RestController
@RequestMapping("/api/room-amenities")
public class RoomAmenityController {

    private final RoomAmenityService service;

    public RoomAmenityController(RoomAmenityService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('rooms:view', 'rooms:manage', 'ADMIN')")
    public ResponseEntity<List<RoomAmenityResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // Admin only
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomAmenityResponse> create(
            @RequestBody RoomAmenityCreateRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}