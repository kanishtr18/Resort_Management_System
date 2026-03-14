/**
 * AccountLedger.java
 * Purpose:
 *  - Entity for accounting ledger/accounts used by financial flows (room revenue, F&B, cash).
 * Annotations & fields:
 *  - @Entity, @Table(name = "account_ledger")
 *  - id: UUID PK
 *  - accountCode String (unique)
 *  - name String
 *  - accountType enum (ASSET/LIABILITY/REVENUE/EXPENSE)
 *  - balance BigDecimal (nullable, use BigDecimal)
 *  - currency String (ISO)
 *  - @Version Long version (optimistic locking)
 *  - extends Auditable (since ledger updates are critical for audit)
 *  - soft-delete not required but allowed for archival
 *
 * Behavior:
 *  - Keep minimal logic in entity (no methods that mutate business state).
 *  - Use Repository for CRUD and Service for transactional updates.
 *
 * Usage:
 *  - Ledger adjustments performed through AccountLedgerService (transactional).
 *
 * File: billing/entity/AccountLedger.java
 */
package com.resortmanagement.system.billing.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account_ledger")
public class AccountLedger extends Auditable {

    @Id
    @GeneratedValue(generator="UUID")
    @UuidGenerator
    @Column(name = "ledger_id")
    private UUID id;

    @NotBlank
    @Column(name = "account_code", nullable = false, unique = true, length = 50)
    private String accountCode;

    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private AccountType accountType;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "currency")
    private String currency = "INR";

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AccountLedger))
            return false;
        AccountLedger ledger = (AccountLedger) o;
        return id != null && id.equals(ledger.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }
}