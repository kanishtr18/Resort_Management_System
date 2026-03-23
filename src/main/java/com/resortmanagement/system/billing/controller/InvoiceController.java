// package com.resortmanagement.system.billing.controller;

// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.resortmanagement.system.billing.dto.InvoiceRequest;
// import com.resortmanagement.system.billing.dto.InvoiceResponse;
// import com.resortmanagement.system.billing.entity.Invoice;
// import com.resortmanagement.system.billing.mapper.BillingMapper;
// import com.resortmanagement.system.billing.service.InvoiceService;

// import jakarta.validation.Valid;

// /**
//  * InvoiceController
//  * Purpose:
//  *  - REST controller for Invoice operations
//  * Endpoints:
//  *  - GET /api/billing/invoices - Get all invoices
//  *  - GET /api/billing/invoices/{id} - Get invoice by ID
//  *  - POST /api/billing/invoices - Create new invoice
//  *  - PUT /api/billing/invoices/{id} - Update invoice
//  *  - POST /api/billing/invoices/{id}/issue - Issue invoice (DRAFT -> ISSUED)
//  */
// @RestController
// @RequestMapping("/api/invoices")
// public class InvoiceController {

//     private final InvoiceService service;

//     public InvoiceController(
//         InvoiceService service
//     ) {
//         this.service = service;
//     }

//     @GetMapping
//     public ResponseEntity<List<InvoiceResponse>> getAll() {
//         return ResponseEntity.ok(service.findAll().stream()
//                 .map(BillingMapper::toResponse)
//                 .collect(Collectors.toList()));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<InvoiceResponse> getById(@PathVariable UUID id) {
//         return service.findById(id)
//                 .map(BillingMapper::toResponse)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping
//     public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody InvoiceRequest request) {
//         Invoice invoice = BillingMapper.toEntity(request);
//         invoice.setFolio(service.getFolioForInvoice(request.getFolioId()));
//         invoice.setReservation(service.getReservationForInvoice(request.getReservationId()));
//         Invoice created = service.save(invoice);
//         return ResponseEntity.status(HttpStatus.CREATED).body(BillingMapper.toResponse(created));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<InvoiceResponse> update(@PathVariable UUID id, @Valid @RequestBody InvoiceRequest request) {
//         if (!service.findById(id).isPresent()) {
//             return ResponseEntity.notFound().build();
//         }
//         Invoice invoice = BillingMapper.toEntity(request);
//         invoice.setId(id);
//         invoice.setFolio(service.getFolioForInvoice(id));
//         invoice.setReservation(service.getReservationForInvoice(id));
//         Invoice updated = service.save(invoice);
//         return ResponseEntity.ok(BillingMapper.toResponse(updated));
//     }

//     @PostMapping("/{id}/issue")
//     public ResponseEntity<InvoiceResponse> issueInvoice(@PathVariable UUID id) {
//         Invoice issued = service.issueInvoice(id);
//         return ResponseEntity.ok(BillingMapper.toResponse(issued));
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

import com.resortmanagement.system.billing.dto.InvoiceRequest;
import com.resortmanagement.system.billing.dto.InvoiceResponse;
import com.resortmanagement.system.billing.entity.Invoice;
import com.resortmanagement.system.billing.mapper.BillingMapper;
import com.resortmanagement.system.billing.service.InvoiceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/billing/invoices")  // Fix: was /api/invoices
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('billing:view', 'billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<List<InvoiceResponse>> getAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(BillingMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('billing:view', 'billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(BillingMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<InvoiceResponse> create(
            @Valid @RequestBody InvoiceRequest request) {
        Invoice invoice = BillingMapper.toEntity(request);
        invoice.setFolio(service.getFolioForInvoice(request.getFolioId()));
        invoice.setReservation(service.getReservationForInvoice(request.getReservationId()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BillingMapper.toResponse(service.save(invoice)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('billing:create', 'billing:approve', 'ADMIN')")
    public ResponseEntity<InvoiceResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody InvoiceRequest request) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Invoice invoice = BillingMapper.toEntity(request);
        invoice.setId(id);
        // Fix: use IDs from request, not invoice ID
        invoice.setFolio(service.getFolioForInvoice(request.getFolioId()));
        invoice.setReservation(service.getReservationForInvoice(request.getReservationId()));
        return ResponseEntity.ok(BillingMapper.toResponse(service.save(invoice)));
    }

    @PostMapping("/{id}/issue")
    @PreAuthorize("hasAnyAuthority('billing:approve', 'ADMIN')")
    public ResponseEntity<InvoiceResponse> issueInvoice(@PathVariable UUID id) {
        return ResponseEntity.ok(BillingMapper.toResponse(service.issueInvoice(id)));
    }
}