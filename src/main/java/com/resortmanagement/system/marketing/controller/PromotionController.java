package com.resortmanagement.system.marketing.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.marketing.dto.promotion.PromotionRequest;
import com.resortmanagement.system.marketing.dto.promotion.PromotionResponse;
import com.resortmanagement.system.marketing.service.PromotionService;

@RestController
@RequestMapping("/api/marketing/promotions")
public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService promotionService) {
        this.service = promotionService;
    }

    @GetMapping
    public ResponseEntity<Page<PromotionResponse>> getAllPromotions(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody PromotionRequest request) {
        PromotionResponse created = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponse> updatePromotion(
            @PathVariable UUID id,
            @RequestBody PromotionRequest request) {
        PromotionResponse updated = service.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
