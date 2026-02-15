package com.resortmanagement.system.billing.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.billing.dto.FolioRequest;
import com.resortmanagement.system.billing.dto.FolioResponse;
import com.resortmanagement.system.billing.entity.Folio;
import com.resortmanagement.system.billing.mapper.BillingMapper;
import com.resortmanagement.system.billing.service.FolioService;

import jakarta.validation.Valid;

/**
 * FolioController
 * Purpose:
 *  - REST controller for Folio operations
 * Endpoints:
 *  - GET /api/billing/folios - Get all folios
 *  - GET /api/billing/folios/{id} - Get folio by ID
 *  - POST /api/billing/folios - Create new folio
 *  - PUT /api/billing/folios/{id} - Update folio
 *  - POST /api/billing/folios/{id}/close - Close folio (state transition)
 *  - POST /api/billing/folios/{id}/void - Void folio (for incorrect/cancelled folios)
 */
@RestController
@RequestMapping("/api/billing/folios")
public class FolioController {

    private final FolioService service;
    
    public FolioController(FolioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FolioResponse>> getAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(BillingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolioResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(BillingMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FolioResponse> create(@Valid @RequestBody FolioRequest request) {
        FolioResponse response = service.createFolioForReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FolioResponse> update(@PathVariable UUID id, @Valid @RequestBody FolioRequest request) {
        FolioResponse updated = service.updateFolio(id, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<FolioResponse> closeFolio(@PathVariable UUID id) {
        Folio closed = service.closeFolio(id);
        FolioResponse response = BillingMapper.toResponse(closed);
        response.setReservationId(closed.getReservation().getId());
        response.setBookingGuestId(closed.getBookingGuest().getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/void")
    public ResponseEntity<FolioResponse> voidFolio(@PathVariable UUID id) {
        Folio voided = service.voidFolio(id);
        FolioResponse response = BillingMapper.toResponse(voided);
        response.setReservationId(voided.getReservation().getId());
        response.setBookingGuestId(voided.getBookingGuest().getId());
        return ResponseEntity.ok(response);
    }
}
