/*
ReservationServiceBookingController.java
Purpose:
 - Expose endpoints for services (staff-delivered) associated to reservations (spa, shuttle).
Endpoints:
 - POST /api/reservations/{id}/services -> schedule a service
 - GET /api/reservations/{id}/services
Responsibilities:
 - Validate service availability with ServiceItemService.
 - Create ReservationServiceBooking entries via ReservationServiceBookingService (transactional).
 - Assign service to folio for billing.
File: booking/controller/ReservationServiceBookingController.java
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

import com.resortmanagement.system.booking.dto.request.ReservationServiceBookingRequest;
import com.resortmanagement.system.booking.dto.response.ReservationServiceBookingResponse;
import com.resortmanagement.system.booking.service.ReservationServiceBookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/booking/reservationservicebookings")
public class ReservationServiceBookingController {

    private final ReservationServiceBookingService service;

    public ReservationServiceBookingController(ReservationServiceBookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationServiceBookingResponse> bookService(
            @PathVariable UUID reservationId,
            @RequestBody @Valid ReservationServiceBookingRequest request) {
        return ResponseEntity.ok(service.bookService(reservationId, request));
    }

    @DeleteMapping("/{serviceBookingId}")
    public ResponseEntity<Void> cancelServiceBooking(
            @PathVariable UUID serviceBookingId) {
        service.cancelServiceBooking(serviceBookingId);
        return ResponseEntity.noContent().build();
    }
}
