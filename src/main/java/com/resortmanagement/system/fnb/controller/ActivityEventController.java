// package com.resortmanagement.system.fnb.controller;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.resortmanagement.system.fnb.service.ActivityEventService;

// @RestController
// @RequestMapping("/api/fnb/activity-events")
// public class ActivityEventController {

//     private final ActivityEventService service;

//     public ActivityEventController(ActivityEventService service) {
//         this.service = service;
//     }

//     /**
//      * Get all active activity events
//      */
//     @GetMapping
//     public ResponseEntity<List<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse>> getAll(
//             @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
//         if (activeOnly) {
//             return ResponseEntity.ok(service.findAllActive());
//         }
//         return ResponseEntity.ok(service.findAll());
//     }

//     /**
//      * Get activity event by ID
//      */
//     @GetMapping("/{id}")
//     public ResponseEntity<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> getById(
//             @PathVariable UUID id) {
//         return service.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     /**
//      * Create an activity event
//      */
//     @PostMapping
//     public ResponseEntity<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> create(
//             @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.ActivityEventRequest request) {
//         com.resortmanagement.system.fnb.dto.response.ActivityEventResponse saved = service.create(request);
//         return new ResponseEntity<>(saved, HttpStatus.CREATED);
//     }

//     /**
//      * Update an activity event
//      */
//     @PutMapping("/{id}")
//     public ResponseEntity<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> update(
//             @PathVariable UUID id,
//             @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.ActivityEventRequest request) {
//         return ResponseEntity.ok(service.update(id, request));
//     }

//     /**
//      * Soft delete an activity event
//      */
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> delete(@PathVariable UUID id) {
//         service.delete(id);
//         return ResponseEntity.noContent().build();
//     }
// }
package com.resortmanagement.system.fnb.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.fnb.dto.request.ActivityEventRequest;
import com.resortmanagement.system.fnb.dto.response.ActivityEventResponse;
import com.resortmanagement.system.fnb.service.ActivityEventService;

@RestController
@RequestMapping("/api/fnb/activity-events")
public class ActivityEventController {

    private final ActivityEventService service;

    public ActivityEventController(ActivityEventService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<List<ActivityEventResponse>> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(activeOnly ? service.findAllActive() : service.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<ActivityEventResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<ActivityEventResponse> create(
            @jakarta.validation.Valid @RequestBody ActivityEventRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<ActivityEventResponse> update(
            @PathVariable UUID id,
            @jakarta.validation.Valid @RequestBody ActivityEventRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}