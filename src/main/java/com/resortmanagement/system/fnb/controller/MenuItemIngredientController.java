// package com.resortmanagement.system.fnb.controller;

// import com.resortmanagement.system.fnb.entity.MenuItemIngredient;
// import com.resortmanagement.system.fnb.service.MenuItemIngredientService;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/fnb/menu-item-ingredients")
// public class MenuItemIngredientController {

//     private final MenuItemIngredientService service;

//     public MenuItemIngredientController(MenuItemIngredientService service) {
//         this.service = service;
//     }

//     /**
//      * Get all menu item ingredients (recipes)
//      */
//     @GetMapping
//     public ResponseEntity<List<MenuItemIngredient>> getAll() {
//         return ResponseEntity.ok(service.findAll());
//     }

//     /**
//      * Get menu item ingredient by ID
//      */
//     @GetMapping("/{id}")
//     public ResponseEntity<MenuItemIngredient> getById(@PathVariable UUID id) {
//         return service.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     /**
//      * Get all ingredients for a specific menu item
//      */
//     @GetMapping("/menu-item/{menuItemId}")
//     public ResponseEntity<List<MenuItemIngredient>> getByMenuItem(
//             @PathVariable UUID menuItemId) {
//         return ResponseEntity.ok(service.findByMenuItem(menuItemId));
//     }

//     /**
//      * Create a menu item ingredient (recipe mapping)
//      */
//     @PostMapping
//     public ResponseEntity<MenuItemIngredient> create(
//             @RequestBody MenuItemIngredient entity) {
//         MenuItemIngredient saved = service.save(entity);
//         return new ResponseEntity<>(saved, HttpStatus.CREATED);
//     }

//     /**
//      * Update a menu item ingredient
//      */
//     @PutMapping("/{id}")
//     public ResponseEntity<MenuItemIngredient> update(
//             @PathVariable UUID id,
//             @RequestBody MenuItemIngredient entity) {

//         // ensure correct ID is used
//         entity.setId(id);
//         return ResponseEntity.ok(service.save(entity));
//     }

//     /**
//      * Delete a menu item ingredient (hard delete allowed)
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

import com.resortmanagement.system.fnb.dto.request.MenuItemIngredientRequest;
import com.resortmanagement.system.fnb.dto.response.MenuItemIngredientResponse;
import com.resortmanagement.system.fnb.service.MenuItemIngredientService;

@RestController
@RequestMapping("/api/fnb/menu-item-ingredients")
public class MenuItemIngredientController {

    private final MenuItemIngredientService service;

    public MenuItemIngredientController(MenuItemIngredientService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<List<MenuItemIngredientResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<MenuItemIngredientResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu-item/{menuItemId}")
    @PreAuthorize("hasAnyAuthority('fnb:view', 'fnb:manage', 'ADMIN')")
    public ResponseEntity<List<MenuItemIngredientResponse>> getByMenuItem(
            @PathVariable UUID menuItemId) {
        return ResponseEntity.ok(service.findByMenuItem(menuItemId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<MenuItemIngredientResponse> create(
            @RequestBody MenuItemIngredientRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<MenuItemIngredientResponse> update(
            @PathVariable UUID id,
            @RequestBody MenuItemIngredientRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('fnb:manage', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}