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

// @RestController
// @RequestMapping("/api/fnb/menu-items")
// public class MenuItemController {

//     private final com.resortmanagement.system.fnb.service.MenuItemService service;

//     public MenuItemController(com.resortmanagement.system.fnb.service.MenuItemService service) {
//         this.service = service;
//     }

//     /**
//      * Get all active menu items
//      */
//     @GetMapping
//     public ResponseEntity<List<com.resortmanagement.system.fnb.dto.response.MenuItemResponse>> getAll(
//             @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
//         if (activeOnly) {
//             return ResponseEntity.ok(service.findAllActive());
//         }
//         return ResponseEntity.ok(service.findAll());
//     }

//     /**
//      * Get menu item by ID
//      */
//     @GetMapping("/{id}")
//     public ResponseEntity<com.resortmanagement.system.fnb.dto.response.MenuItemResponse> getById(
//             @PathVariable UUID id) {
//         return service.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     /**
//      * Create a menu item
//      */
//     @PostMapping
//     public ResponseEntity<com.resortmanagement.system.fnb.dto.response.MenuItemResponse> create(
//             @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.MenuItemRequest request) {
//         com.resortmanagement.system.fnb.dto.response.MenuItemResponse saved = service.create(request);
//         return new ResponseEntity<>(saved, HttpStatus.CREATED);
//     }

//     /**
//      * Update a menu item
//      */
//     @PutMapping("/{id}")
//     public ResponseEntity<com.resortmanagement.system.fnb.dto.response.MenuItemResponse> update(
//             @PathVariable UUID id,
//             @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.MenuItemRequest request) {
//         return ResponseEntity.ok(service.update(id, request));
//     }

//     /**
//      * Soft delete a menu item
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

import com.resortmanagement.system.fnb.dto.request.MenuItemRequest;
import com.resortmanagement.system.fnb.dto.response.MenuItemResponse;
import com.resortmanagement.system.fnb.service.MenuItemService;

@RestController
@RequestMapping("/api/fnb/menu-items")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<List<MenuItemResponse>> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(activeOnly ? service.findAllActive() : service.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> create(
            @jakarta.validation.Valid @RequestBody MenuItemRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable UUID id,
            @jakarta.validation.Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}