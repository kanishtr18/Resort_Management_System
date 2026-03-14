/**
 * Invoice.java
 * Purpose:
 *  - Persisted financial document generated from folio charges.
 * Fields:
 *  - id: UUID
 *  - folioId: UUID (reference to Folio)
 *  - reservationId: UUID (optional)
 *  - guestId: UUID
 *  - issueDate: Instant
 *  - dueDate: Instant
 *  - totalAmount: BigDecimal
 *  - taxAmount: BigDecimal
 *  - status enum (DRAFT, ISSUED, PAID, PARTIALLY_PAID, REFUNDED, CANCELLED)
 *  - currency String
 *  - extends Auditable (must track who issued)
 *  - store request/response snapshot JSON if needed (for dispute)
 * Notes:
 *  - Immutable once ISSUED except for credit/refund operations stored separately.
 *  - Use InvoiceService to compute totals; never compute in controllers.
 *
 * File: billing/entity/Invoice.java
 */
package com.resortmanagement.system.billing.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.audit.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "invoice")
public class Invoice extends Auditable {

    @Id
    @GeneratedValue(generator="UUID")
    @UuidGenerator
    @Column(name = "invoice_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 64)
    private String invoiceNumber;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(name = "currency")
    private String currency = "INR";

    @Version
    @Column(name = "version")
    private Long version;

    // JPA Relationships - Financial record chain: Folio -> Invoice -> Payment ->
    // Refund

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folio_id")
    private Folio folio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation; 

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Invoice))
            return false;
        Invoice invoice = (Invoice) o;
        return id != null && id.equals(invoice.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }
}