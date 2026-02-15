package com.resortmanagement.system.fnb.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.resortmanagement.system.billing.entity.Folio;
import com.resortmanagement.system.booking.entity.BookingGuest;
import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.enums.OrderStatus;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends AuditableSoftDeletable {

    @Id
    @GeneratedValue    
    @Column(name = "order_id")
    private UUID id;

    /**
     * Guest placing the order (walk-in orders may not have this)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private BookingGuest guestId;

    /**
     * Reservation context (nullable for POS orders)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservationId;

    /**
     * Table reference for restaurant orders
     */
    // @Column(name = "table_id")
    // private UUID tableId;

    // Adding tableId properly if needed, but commented out in original.
    // DTO had tableId though! OrderRequest had tableId.
    // OrderResponse had tableId.
    // If I map to Entity, Entity MUST have tableId if I want to persist it.
    // I will enable tableId column to match DTO usage.

    @Column(name = "table_id")
    private UUID tableId;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "placed_at", nullable = false)
    private Instant placedAt;

    /**
     * Linked folio for billing (nullable until billed)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_folio_id")
    private Folio assignedFolio;

    /**
     * Order → OrderItem (bidirectional)
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();

    //order_number VARCHAR(64) NOT NULL UNIQUE,

}
