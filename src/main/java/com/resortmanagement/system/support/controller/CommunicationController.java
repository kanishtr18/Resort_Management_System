package com.resortmanagement.system.support.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.support.dto.request.CommunicationCreateRequest;
import com.resortmanagement.system.support.dto.response.CommunicationResponse;
import com.resortmanagement.system.support.service.CommunicationService;

@RestController
@RequestMapping("/api/communications")
public class CommunicationController {

    private final CommunicationService service;

    public CommunicationController(CommunicationService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('support:view', 'support:manage', 'ADMIN')")
    public ResponseEntity<List<CommunicationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<CommunicationResponse> create(
            @RequestBody CommunicationCreateRequest req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}