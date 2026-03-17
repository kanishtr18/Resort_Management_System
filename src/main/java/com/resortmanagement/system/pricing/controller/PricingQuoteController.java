package com.resortmanagement.system.pricing.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.pricing.dto.request.PricingQuoteRequest;
import com.resortmanagement.system.pricing.dto.response.PricingQuoteResponse;
import com.resortmanagement.system.pricing.service.PricingQuoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingQuoteController {

    private final PricingQuoteService service;

    @PostMapping("/quote")
    public PricingQuoteResponse quote(@Valid @RequestBody PricingQuoteRequest request) {
        return service.calculate(request);
    }
}
