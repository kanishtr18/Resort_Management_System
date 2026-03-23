package com.resortmanagement.system.support.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.support.dto.request.FeedbackReviewRequest;
import com.resortmanagement.system.support.dto.response.FeedbackReviewResponse;
import com.resortmanagement.system.support.mapper.FeedbackReviewMapper;
import com.resortmanagement.system.support.service.FeedbackReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackReviewController {

    private final FeedbackReviewService service;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('support:view', 'support:manage', 'ADMIN')")
    public ResponseEntity<List<FeedbackReviewResponse>> getAll() {
        return ResponseEntity.ok(
            service.getAll().stream()
                .map(FeedbackReviewMapper::toResponse)
                .toList()
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<FeedbackReviewResponse> create(
            @RequestBody FeedbackReviewRequest request) {
        return new ResponseEntity<>(
            FeedbackReviewMapper.toResponse(service.create(request)),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/respond/{staffId}")
    @PreAuthorize("hasAnyAuthority('support:manage', 'ADMIN')")
    public ResponseEntity<FeedbackReviewResponse> respond(
            @PathVariable UUID id,
            @PathVariable UUID staffId) {
        return ResponseEntity.ok(
            FeedbackReviewMapper.toResponse(service.respond(id, staffId))
        );
    }
}