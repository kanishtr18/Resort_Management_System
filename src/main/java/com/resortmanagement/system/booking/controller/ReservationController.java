/*
ReservationController.java
Purpose:
 - Core booking endpoints: create reservation, modify, confirm, cancel, checkin, checkout.
Suggested endpoints:
 - POST /api/reservations -> create reservation (CreateReservationRequest)
 - GET /api/reservations/{id}
 - POST /api/reservations/{id}/confirm
 - POST /api/reservations/{id}/cancel
 - POST /api/reservations/{id}/checkin
 - POST /api/reservations/{id}/checkout
Responsibilities:
 - Validate request DTOs and call ReservationService.
 - Return ReservationResponse DTO (never entity).
 - Use proper HTTP codes (201 created, 202 accepted for async, 409 for no availability).
 - Implement PATCH endpoints for partial updates if necessary.
Security:
 - restrict checkin/checkout to ROLE_FRONTDESK or ROLE_MANAGER.

File: booking/controller/ReservationController.java
*/

package com.resortmanagement.system.booking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.booking.dto.request.ReservationCreateRequest;
import com.resortmanagement.system.booking.dto.request.ReservationUpdateRequest;
import com.resortmanagement.system.booking.dto.response.ReservationDetailResponse;
import com.resortmanagement.system.booking.dto.response.ReservationResponse;
import com.resortmanagement.system.booking.service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/booking/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService
    
    service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('reservations:create', 'ADMIN')")
    public ResponseEntity<ReservationDetailResponse> createReservation(
            @RequestBody @Valid ReservationCreateRequest request) {
        return ResponseEntity.ok(service.createReservation(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('reservations:view', 'reservations:create', 'reservations:edit', 'reservations:delete', 'ADMIN')")
    public ResponseEntity<List<ReservationResponse>> listReservations() {
        return ResponseEntity.ok(service.listReservations());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('reservations:view', 'reservations:create', 'reservations:edit', 'reservations:delete', 'ADMIN')")
    public ResponseEntity<ReservationDetailResponse> getReservationDetail(
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getReservation(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('reservations:edit', 'ADMIN')")
    public ResponseEntity<Void> updateReservation(
        @PathVariable UUID id,
        @RequestBody @Valid ReservationUpdateRequest request) {
    service.updateReservation(id, request);
    return ResponseEntity.noContent().build();  // 204 is correct for PUT with no body
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('reservations:delete', 'ADMIN')")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID id) {
        service.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
