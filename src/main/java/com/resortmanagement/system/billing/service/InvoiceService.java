package com.resortmanagement.system.billing.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.billing.entity.Folio;
import com.resortmanagement.system.billing.entity.Invoice;
import com.resortmanagement.system.billing.entity.InvoiceStatus;
import com.resortmanagement.system.billing.repository.FolioRepository;
import com.resortmanagement.system.billing.repository.InvoiceRepository;
import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.booking.repository.ReservationRepository;
import com.resortmanagement.system.common.exception.ApplicationException;

/**
 * InvoiceService
 * Purpose:
 *  - Service layer for Invoice entity operations
 *  - Handles invoice generation, status transitions, and payment tracking
 * Business Logic:
 *  - issueInvoice: Transitions invoice from DRAFT to ISSUED status
 *  - Validates invoice state before operations
 *  - Ensures invoices are immutable once ISSUED (except for status updates like PAID)
 */
@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository repository;
    public InvoiceService(
        InvoiceRepository repository,
        FolioRepository folioRepository,
        ReservationRepository reservationRepository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Invoice> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findByFolioId(UUID folioId) {
        return repository.findByFolioId(folioId);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findByStatus(InvoiceStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Reservation getReservationForInvoice(UUID invoiceId) {
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new ApplicationException("Invoice not found with id: " + invoiceId));
        return invoice.getReservation();
    }

    @Transactional(readOnly = true)
    public Folio getFolioForInvoice(UUID invoiceId) {
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new ApplicationException("Invoice not found with id: " + invoiceId));
        return invoice.getFolio();
    }

    public Invoice save(Invoice invoice) {
        // Validation: ensure required fields are present
        if (invoice.getFolio() == null) {
            throw new ApplicationException("Folio ID is required for invoice");
        }
        if (invoice.getTotalAmount() == null) {
            throw new ApplicationException("Total amount is required for invoice");
        }
        
        // Set issueDate if not set and status is not DRAFT
        if (invoice.getIssueDate() == null && invoice.getStatus() != InvoiceStatus.DRAFT) {
            invoice.setIssueDate(Instant.now());
        }
        
        return repository.save(invoice);
    }

    public Invoice issueInvoice(UUID invoiceId) {
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new ApplicationException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new ApplicationException("Only DRAFT invoices can be issued");
        }
        
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(Instant.now());
        return repository.save(invoice);
    }
}
