/*
ReservationAddOn.java
Purpose:
 - Represents add-on items tied to a reservation.
Fields:
 - id: UUID
 - reservation: Reservation (ManyToOne)
 - addOnType: enum or reference to a product (prefer FK to a service or package component)
 - qty: int
 - price: BigDecimal (snapshot)
 - extends Auditable
Notes:
 - Add-on price must be snapshotted at creation and assigned to a folio.
 - Use validation: qty > 0.

File: booking/entity/ReservationAddOn.java
*/

package com.resortmanagement.system.booking.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.enums.AddOnStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reservation_add_on")
public class ReservationAddOn extends AuditableSoftDeletable {
    @Id
    @UuidGenerator
    @Column(name = "reservation_add_on_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "add_on_code", nullable = false)
    private String addOnCode; // e.g. "SPA_PACKAGE", "LATE_CHECKOUT"

    @Column(name = "add_on_name", nullable = false)
    private String addOnName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;
    
    @Column(name = "total_price", nullable = false)
    private Double totalPrice; // price * quantity

    @Enumerated(EnumType.STRING)
    private AddOnStatus status; // e.g. ACTIVE, CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
}

