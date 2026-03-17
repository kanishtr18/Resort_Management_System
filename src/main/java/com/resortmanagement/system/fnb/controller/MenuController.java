/*
MenuController.java
Purpose:
 - Manage menus and menu grouping (Breakfast, Bar).
Endpoints:
 - POST /api/menus
 - GET /api/menus
 - GET /api/menus/{id}
Responsibilities:
 - Minimal logic; MenuService handles creation and item assignment.
File: fnb/controller/MenuController.java
*/
package com.resortmanagement.system.fnb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.fnb.service.MenuService;

@RestController
@RequestMapping("/api/fnb/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<List<com.resortmanagement.system.fnb.dto.response.MenuResponse>> getAll(
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        if (activeOnly) {
            return ResponseEntity.ok(this.menuService.findAllActive());
        }
        return ResponseEntity.ok(this.menuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.fnb.dto.response.MenuResponse> getById(
            @PathVariable java.util.UUID id) {
        return this.menuService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<com.resortmanagement.system.fnb.dto.response.MenuResponse> create(
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.MenuRequest request) {
        return ResponseEntity.ok(this.menuService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.resortmanagement.system.fnb.dto.response.MenuResponse> update(
            @PathVariable java.util.UUID id,
            @jakarta.validation.Valid @RequestBody com.resortmanagement.system.fnb.dto.request.MenuRequest request) {
        return ResponseEntity.ok(this.menuService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable java.util.UUID id) {
        this.menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
