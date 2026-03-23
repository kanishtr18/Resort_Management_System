package com.resortmanagement.system.booking.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.booking.dto.request.ReservationRoomAssignmentRequest;
import com.resortmanagement.system.booking.dto.response.ReservationRoomAssignmentResponse;
import com.resortmanagement.system.booking.service.ReservationRoomAssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings/room-assignments")
public class ReservationRoomAssignmentController {

    private final ReservationRoomAssignmentService service;

    public ReservationRoomAssignmentController(ReservationRoomAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/reservation/{reservationId}")
    @PreAuthorize("hasAnyAuthority('reservations:edit', 'ADMIN')")
    public ResponseEntity<ReservationRoomAssignmentResponse> assignRoom(
        @PathVariable UUID reservationId,
        @RequestBody @Valid ReservationRoomAssignmentRequest request) {
    return ResponseEntity.ok(service.assignRoom(reservationId, request));
    }

    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasAnyAuthority('reservations:edit', 'ADMIN')")
    public ResponseEntity<Void> unassignRoom(
            @PathVariable UUID assignmentId
    ) {
        service.unassignRoom(assignmentId);
        return ResponseEntity.noContent().build();
    }
}