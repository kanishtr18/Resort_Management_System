/*
BookingGuestController.java
Purpose:
 - CRUD endpoints for booking guest records (booking/occupant details).
Endpoints:
 - POST /api/v1/reservations/{reservationId}/guests -> add guest to reservation
 - GET /api/v1/reservations/{reservationId}/guests -> list guests
 - DELETE /api/v1/reservations/{reservationId}/guests/{id} -> remove guest

Responsibilities:
 - Validate booking occupancy limits against roomType.maxOccupancy.
 - Use BookingGuestService for transactional updates.
 - DTOs: BookingGuestRequest, BookingGuestResponse.
 - Use proper status codes and handle 409 when over-capacity.

File: booking/controller/BookingGuestController.java
*/

package com.resortmanagement.system.booking.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.booking.service.BookingGuestService;

@RestController
@RequestMapping("/api/booking/bookingguests")
public class BookingGuestController {

    private final BookingGuestService service;

    public BookingGuestController(BookingGuestService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeGuestFromReservation(
        @PathVariable UUID id
    ) {
        service.removeGuest(id);
        return ResponseEntity.noContent().build();
    }
}
