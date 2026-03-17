/*
RatePlanController.java
Purpose:
 - CRUD for RatePlan.
Endpoints:
 - POST /api/rate-plans
 - GET /api/rate-plans/{id}
Responsibilities:
 - RatePlan defines cancellation policy, base price, min/max stay, refundable flag.
File: pricing/controller/RatePlanController.java
*/
package com.resortmanagement.system.pricing.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.pricing.dto.request.RatePlanCreateRequest;
import com.resortmanagement.system.pricing.dto.request.RatePlanUpdateRequest;
import com.resortmanagement.system.pricing.dto.response.RatePlanResponse;
import com.resortmanagement.system.pricing.dto.response.RatePlanSummaryResponse;
import com.resortmanagement.system.pricing.service.RatePlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rate-plans")
@RequiredArgsConstructor
public class RatePlanController {

    private final RatePlanService service;

    @PostMapping
    public RatePlanResponse create(@Valid @RequestBody RatePlanCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<RatePlanResponse> getAll() {
        return service.getAllRatePlan();
    }

    @GetMapping("/{id}")
    public RatePlanResponse get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public RatePlanResponse update(@PathVariable UUID id,
            @RequestBody RatePlanUpdateRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/summary")
    public List<RatePlanSummaryResponse> getAllRatePlanSummary() {
        return service.getAllRatePlanSummary();
    }
}
