// package com.resortmanagement.system.billing.controller;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.resortmanagement.system.billing.dto.PaymentRequest;
// import com.resortmanagement.system.billing.dto.PaymentResponse;
// import com.resortmanagement.system.billing.entity.Payment;
// import com.resortmanagement.system.billing.mapper.BillingMapper;
// import com.resortmanagement.system.billing.service.PaymentService;
// import java.util.stream.Collectors;

// import jakarta.validation.Valid;

// /**
//  * PaymentController
//  * Purpose:
//  *  - REST controller for Payment operations
//  * Endpoints:
//  *  - GET /api/billing/payments - Get all payments
//  *  - GET /api/billing/payments/{id} - Get payment by ID
//  *  - POST /api/billing/payments - Create new payment
//  *  - POST /api/billing/payments/{id}/process - Process payment (PENDING -> SUCCESS/FAILED)
//  */
// @RestController
// @RequestMapping("/api/payments")
// public class PaymentController {

//     private final PaymentService service;

//     public PaymentController(PaymentService service) {
//         this.service = service;
//     }

//     @GetMapping
//     public ResponseEntity<List<PaymentResponse>> getAll() {
//         return ResponseEntity.ok(service.findAll().stream()
//                 .map(BillingMapper::toResponse)
//                 .collect(Collectors.toList()));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<PaymentResponse> getById(@PathVariable UUID id) {
//         return service.findById(id)
//                 .map(BillingMapper::toResponse)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping
//     public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
//         Payment payment = BillingMapper.toEntity(request);
//         payment.setInvoice(service.getInvoice(request.getInvoiceId()));
//         Payment created = service.save(payment);
//         return ResponseEntity.status(HttpStatus.CREATED).body(BillingMapper.toResponse(created));
//     }

//     @PostMapping("/{id}/process")
//     public ResponseEntity<PaymentResponse> processPayment(
//             @PathVariable UUID id,
//             @RequestParam boolean success,
//             @RequestParam(required = false) String providerResponse) {
//         Payment processed = service.processPayment(id, success, providerResponse);
//         return ResponseEntity.ok(BillingMapper.toResponse(processed));
//     }
// }

package com.resortmanagement.system.billing.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.billing.dto.PaymentRequest;
import com.resortmanagement.system.billing.dto.PaymentResponse;
import com.resortmanagement.system.billing.entity.Payment;
import com.resortmanagement.system.billing.mapper.BillingMapper;
import com.resortmanagement.system.billing.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/billing/payments")  // Fix: was /api/payments
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('billing:view', 'billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(BillingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('billing:view', 'billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<PaymentResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(BillingMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody PaymentRequest request) {
        Payment payment = BillingMapper.toEntity(request);
        payment.setInvoice(service.getInvoice(request.getInvoiceId()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BillingMapper.toResponse(service.save(payment)));
    }

    @PostMapping("/{id}/process")
    @PreAuthorize("hasAnyAuthority('billing:approve', 'ADMIN')")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable UUID id,
            @RequestParam boolean success,
            @RequestParam(required = false) String providerResponse) {
        return ResponseEntity.ok(
                BillingMapper.toResponse(service.processPayment(id, success, providerResponse)));
    }
}