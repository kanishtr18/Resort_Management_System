package com.resortmanagement.system.marketing.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.marketing.dto.loyaltymember.LoyaltyMemberRequest;
import com.resortmanagement.system.marketing.dto.loyaltymember.LoyaltyMemberResponse;
import com.resortmanagement.system.marketing.service.LoyaltyMemberService;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyMemberController {

    private final LoyaltyMemberService service;

    public LoyaltyMemberController(LoyaltyMemberService loyaltyMemberService) {
        this.service = loyaltyMemberService;
    }

    @GetMapping
    public ResponseEntity<Page<LoyaltyMemberResponse>> getAllLoyaltyMembers(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyMemberResponse> getLoyaltyMemberById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LoyaltyMemberResponse> createLoyaltyMember(@RequestBody LoyaltyMemberRequest request) {
        LoyaltyMemberResponse created = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyMemberResponse> updateLoyaltyMember(
            @PathVariable UUID id,
            @RequestBody LoyaltyMemberRequest request) {
        LoyaltyMemberResponse updated = service.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoyaltyMember(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<LoyaltyMemberResponse> getLoyaltyMemberByGuestId(@PathVariable UUID guestId) {
        return service.findByGuestId(guestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
