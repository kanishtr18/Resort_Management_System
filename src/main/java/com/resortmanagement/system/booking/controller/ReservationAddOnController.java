/*
ReservationAddOnController.java
Purpose:
 - Manage add-ons attached to reservations (e.g., breakfast, airport pickup).
Endpoints:
 - POST /api/reservations/{id}/addons -> add an add-on
 - GET /api/reservations/{id}/addons
 - DELETE /api/reservations/{id}/addons/{addonId}
Responsibilities:
 - Use ReservationAddOnService (transactional).
 - Validate pricing and apply to folio.
 - DTOs: ReservationAddOnRequest, ReservationAddOnResponse.

File: booking/controller/ReservationAddOnController.java
*/

package com.resortmanagement.system.booking.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.booking.dto.request.ReservationAddOnRequest;
import com.resortmanagement.system.booking.dto.response.ReservationAddOnResponse;
import com.resortmanagement.system.booking.service.ReservationAddOnService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/booking/reservationaddons")
public class ReservationAddOnController {

    private final ReservationAddOnService service;

    public ReservationAddOnController(ReservationAddOnService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationAddOnResponse> addAddOn(
            @PathVariable UUID reservationId,
            @RequestBody @Valid ReservationAddOnRequest request) {
        return ResponseEntity.ok(service.addAddOn(reservationId, request));
    }

    @DeleteMapping("/{addOnId}")
    public ResponseEntity<Void> removeAddOn(
            @PathVariable UUID addOnId) {
        service.removeAddOn(addOnId);
        return ResponseEntity.noContent().build();
    }
}
