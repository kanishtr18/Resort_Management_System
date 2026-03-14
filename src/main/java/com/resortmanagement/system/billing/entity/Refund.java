/**
 * Refund.java
 * Purpose:
 *  - Persist refund operations and statuses.
 * Fields:
 *  - id: UUID
 *  - paymentId: UUID (FK to Payment)
 *  - invoiceId: UUID (optional)
 *  - amount: BigDecimal
 *  - reason: String
 *  - processedBy: String (auditable but keep as field for quick reporting)
 *  - processedAt: Instant
 *  - status: enum (REQUESTED, PROCESSING, SUCCESS, FAILED)
 *  - providerRefundRef: String
 *  - extends Auditable
 * Notes:
 *  - All refund flows must be idempotent and logged in AuditLog with reason.
 *  - Do not perform external PSP ops in the controller; do it in RefundService with retries and idempotency.
 * File: billing/entity/Refund.java
 */
package com.resortmanagement.system.billing.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "refund")
public class Refund extends Auditable {

    @Id
    @GeneratedValue(generator="UUID")
    @UuidGenerator
    @Column(name = "refund_id", updatable = false, nullable = false)
    private UUID id;

    @Version private Long version;
    
    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "processed_by", length = 100)
    private String processedBy;

    @Column(name = "processed_at")
    private Instant processedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RefundStatus status = RefundStatus.REQUESTED;

    @Column(name = "provider_refund_ref")
    private String providerRefundRef;

    // JPA Relationships - Financial record chain: Folio -> Invoice -> Payment ->
    // Refund

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Refund))
            return false;
        Refund refund = (Refund) o;
        return id != null && id.equals(refund.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }
}