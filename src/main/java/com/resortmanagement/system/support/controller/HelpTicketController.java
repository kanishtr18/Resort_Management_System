package com.resortmanagement.system.support.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.support.dto.request.HelpTicketCreateRequest;
import com.resortmanagement.system.support.dto.response.HelpTicketResponse;
import com.resortmanagement.system.support.enums.TicketStatus;
import com.resortmanagement.system.support.service.HelpTicketService;

@RestController
@RequestMapping("/api/support/tickets")
public class HelpTicketController {

    private final HelpTicketService service;

    public HelpTicketController(HelpTicketService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('support:view', 'support:manage', 'ADMIN')")
    public ResponseEntity<List<HelpTicketResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<HelpTicketResponse> create(
            @RequestBody HelpTicketCreateRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<HelpTicketResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<Void> close(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}