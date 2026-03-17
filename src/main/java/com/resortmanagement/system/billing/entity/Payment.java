/**
 * Payment.java
 * Purpose:
 *  - Records payment attempts and results for invoices.
 * Fields:
 *  - id: UUID
 *  - invoiceId: UUID (reference to Invoice)
 *  - reservationId: UUID (nullable)
 *  - guestId: UUID
 *  - amount: BigDecimal
 *  - method: enum (CARD, UPI, CASH, WALLET, BANK_TRANSFER)
 *  - transactionRef: String (PSP reference)
 *  - status: PaymentStatus (PENDING, SUCCESS, FAILED, REFUNDED)
 *  - providerResponse: JSON/string for PSP raw response
 *  - processedAt: Instant
 *  - extends Auditable (important for PCI/compliance trace)
 * Notes:
 *  - Do NOT store card PAN/CVV. Use PSP tokenization.
 *  - Payment creation and invoice update should be handled in a single transactional service method with idempotency.
 *
 * File: billing/entity/Payment.java
 */
package com.resortmanagement.system.billing.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.Auditable;
import com.resortmanagement.system.common.enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "payment")
public class Payment extends Auditable {

    @Id
    @GeneratedValue(generator="UUID")
    @UuidGenerator
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID id;

    @Version private Long version;
    
    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_reference")
    private String transactionRef;

    @Column(name="currency")
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Lob
    @Column(name = "provider_response")
    private String providerResponse;

    @Column(name = "processed_at")
    private Instant processedAt;

    // JPA Relationships - Financial record chain: Folio -> Invoice -> Payment ->
    // Refund

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Refund> refunds = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Payment))
            return false;
        Payment payment = (Payment) o;
        return id != null && id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }
}