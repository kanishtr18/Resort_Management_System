package com.resortmanagement.system.marketing.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.marketing.dto.PackageItemDTO;
import com.resortmanagement.system.marketing.service.PackageItemService;

@RestController
@RequestMapping("/api/marketing/package_items")
public class PackageItemController {

    private final PackageItemService service;

    public PackageItemController(PackageItemService packageItemService) {
        this.service = packageItemService;
    }

    @GetMapping
    public ResponseEntity<List<PackageItemDTO>> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.service.getAllPackageItemDTOs(org.springframework.data.domain.PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageItemDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.getPackageItemDTOById(id));
    }

    @PostMapping
    public ResponseEntity<PackageItemDTO> create(@RequestBody PackageItemDTO dto) {
        if (dto.getPkgId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.service.addPackageItem(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageItemDTO> update(@PathVariable UUID id, @RequestBody PackageItemDTO dto) {
        return ResponseEntity.ok(this.service.updatePackageItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
