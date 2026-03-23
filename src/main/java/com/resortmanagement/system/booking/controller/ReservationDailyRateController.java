/*
ReservationDailyRateController.java
Purpose:
 - Endpoints for maintenance/inspection of daily rates (admin read-only), not general CRUD.
Suggested endpoints:
 - GET /api/reservations/{id}/daily-rates
Responsibilities:
 - Query ReservationDailyRateService to return per-night breakdown for invoices and for UI display.
 - Ensure immutability: do not provide endpoint to modify snapshot rates (admin adjustments go via credits/adjustments).

File: booking/controller/ReservationDailyRateController.java
*/

package com.resortmanagement.system.booking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.booking.dto.response.ReservationDailyRateResponse;
import com.resortmanagement.system.booking.service.ReservationDailyRateService;

@RestController
@RequestMapping("/api/booking/reservationdailyrates")
public class ReservationDailyRateController {

    private final ReservationDailyRateService service;

    public ReservationDailyRateController(ReservationDailyRateService service) {
        this.service = service;
    }

    @GetMapping("/reservation/{reservationId}")
    @PreAuthorize("hasAnyAuthority('reservations:view', 'reservations:edit', 'ADMIN')")
    public ResponseEntity<List<ReservationDailyRateResponse>> getDailyRates(
            @PathVariable UUID reservationId) {
        return ResponseEntity.ok(service.getRates(reservationId));
    }
}
