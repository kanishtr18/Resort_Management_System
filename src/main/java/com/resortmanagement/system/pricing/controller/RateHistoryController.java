/*
 RateHistoryController.java
Purpose:
 - Manage seasonal rate overrides associated to RatePlan.
Endpoints:
 - POST /api/v1/rate-plans/{id}/history
 - GET /api/v1/rate-plans/{id}/history
Responsibilities:
 - Ensure RateHistory date ranges are non-overlapping and validate rules.
File: pricing/controller/RateHistoryController.java
*/
package com.resortmanagement.system.pricing.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.pricing.dto.response.RateHistoryResponse;
import com.resortmanagement.system.pricing.service.RateHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rate-history")
@RequiredArgsConstructor
public class RateHistoryController {

    private final RateHistoryService service;

    @PostMapping("/{ratePlanId}")
    public RateHistoryResponse create(@PathVariable UUID ratePlanId,
                                      @RequestParam Double price,
                                      @RequestParam LocalDate start,
                                      @RequestParam LocalDate end,
                                      @RequestParam(required = false) String seasonName) {
        return service.create(ratePlanId, price, start, end, seasonName);
    }
}

