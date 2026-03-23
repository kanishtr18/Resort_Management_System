// /*
// HousekeepingTaskController.java
// Purpose:
//  - Manage housekeeping tasks: schedule cleanings, mark completed.
// Endpoints:
//  - POST /api/housekeeping/tasks
//  - GET /api/rooms/{roomId}/housekeeping
// Responsibilities:
//  - Create tasks when reservation checks out; assign staff; update room status.
// File: room/controller/HousekeepingTaskController.java
// */
// package com.resortmanagement.system.room.controller;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.resortmanagement.system.room.dto.request.HousekeepingTaskCreateRequest;
// import com.resortmanagement.system.room.dto.request.HousekeepingTaskUpdateRequest;
// import com.resortmanagement.system.room.dto.response.HousekeepingTaskResponse;
// import com.resortmanagement.system.room.service.HousekeepingTaskService;

// @RestController
// @RequestMapping("/api/housekeeping")
// public class HousekeepingTaskController {

//     private final HousekeepingTaskService service;

//     public HousekeepingTaskController(HousekeepingTaskService service) {
//         this.service = service;
//     }

//     @PostMapping
//     public HousekeepingTaskResponse create(@RequestBody HousekeepingTaskCreateRequest req) {
//         return service.create(req);
//     }

//     @GetMapping
// public List<HousekeepingTaskResponse> getAll(
//         @RequestParam(required = false) UUID staffId) {  // ← add this
//     if (staffId != null) {
//         return service.getByStaff(staffId);
//     }
//     return service.getAll();
// }

//     @PutMapping("/{id}")
//     public HousekeepingTaskResponse update(@PathVariable UUID id,
//             @RequestBody HousekeepingTaskUpdateRequest req) {
//         return service.update(id, req);
//     }

//     @DeleteMapping("/{id}")
//     public void delete(@PathVariable UUID id) {
//         service.delete(id);
//     }



// }

package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.HousekeepingTaskCreateRequest;
import com.resortmanagement.system.room.dto.request.HousekeepingTaskUpdateRequest;
import com.resortmanagement.system.room.dto.response.HousekeepingTaskResponse;
import com.resortmanagement.system.room.service.HousekeepingTaskService;

@RestController
@RequestMapping("/api/housekeeping")
public class HousekeepingTaskController {

    private final HousekeepingTaskService service;

    public HousekeepingTaskController(HousekeepingTaskService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('housekeeping:view', 'housekeeping:manage', 'ADMIN')")
    public ResponseEntity<List<HousekeepingTaskResponse>> getAll(
            @RequestParam(required = false) UUID staffId) {
        if (staffId != null) {
            return ResponseEntity.ok(service.getByStaff(staffId));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('housekeeping:manage', 'ADMIN')")
    public ResponseEntity<HousekeepingTaskResponse> create(
            @RequestBody HousekeepingTaskCreateRequest req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('housekeeping:manage', 'ADMIN')")
    public ResponseEntity<HousekeepingTaskResponse> update(
            @PathVariable UUID id,
            @RequestBody HousekeepingTaskUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('housekeeping:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}