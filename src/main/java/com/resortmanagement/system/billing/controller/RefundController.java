package com.resortmanagement.system.billing.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.billing.dto.RefundRequest;
import com.resortmanagement.system.billing.dto.RefundResponse;
import com.resortmanagement.system.billing.entity.Refund;
import com.resortmanagement.system.billing.mapper.BillingMapper;
import com.resortmanagement.system.billing.service.RefundService;

import jakarta.validation.Valid;

/**
 * RefundController
 * Purpose:
 *  - REST controller for Refund operations
 * Endpoints:
 *  - GET /api/billing/refunds - Get all refunds
 *  - GET /api/billing/refunds/{id} - Get refund by ID
 *  - POST /api/billing/refunds - Create new refund request
 *  - POST /api/billing/refunds/{id}/process - Process refund (REQUESTED/PROCESSING -> SUCCESS/FAILED)
 */
@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService service;

    public RefundController(RefundService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RefundResponse>> getAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(BillingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(BillingMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RefundResponse> create(@Valid @RequestBody RefundRequest request) {
        Refund refund = BillingMapper.toEntity(request);
        refund.setPayment(service.getRefund(request.getPaymentId()));
        Refund created = service.save(refund);
        return ResponseEntity.status(HttpStatus.CREATED).body(BillingMapper.toResponse(created));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<RefundResponse> processRefund(
            @PathVariable UUID id,
            @RequestParam boolean success,
            @RequestParam(required = false) String providerRefundRef) {
        Refund processed = service.processRefund(id, success, providerRefundRef);
        return ResponseEntity.ok(BillingMapper.toResponse(processed));
    }
}
