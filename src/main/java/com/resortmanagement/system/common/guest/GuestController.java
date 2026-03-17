/*
GuestController.java
Purpose:
 - CRUD endpoints for guests (create, read, update). Not for booking operations.
Endpoints:
 - POST /api/guests
 - GET /api/guests/{id}
 - PUT /api/guests/{id}
 - DELETE /api/guests/{id} (soft delete)
Responsibilities:
 - Validate input DTOs, avoid returning entity.
 - Use GuestService for operations.
 - GDPR: implement de-identification on delete requests (optionally).
File: common/Guest/GuestController.java
*/

package com.resortmanagement.system.common.guest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.common.guest.dto.guestResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest guest) {
        Guest created = guestService.createGuest(guest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ
    @GetMapping
    public ResponseEntity<List<guestResponseDto>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuest(@PathVariable UUID id) {
        return ResponseEntity.ok(guestService.getGuest(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(
            @PathVariable UUID id,
            @Valid @RequestBody Guest guest) {

        return ResponseEntity.ok(guestService.updateGuest(id, guest));
    }

    // SOFT DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGuest(@PathVariable UUID id) {
        guestService.deleteGuest(id);
    }

    // GDPR ANONYMIZE
    @PostMapping("/{id}/anonymize")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void anonymizeGuest(@PathVariable UUID id) {
        guestService.anonymizeGuest(id);
    }
}