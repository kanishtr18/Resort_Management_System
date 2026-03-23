/**
 * Folio.java
 * Purpose:
 *  - Billing bucket entity; multiple folios per reservation allow split billing.
 * Fields & annotations:
 *  - @Entity @Table("folio")
 *  - id: UUID PK
 *  - reservationId: UUID (nullable for walk-in charges)
 *  - name: String (e.g., "Main Folio", "Incidentals - Guest A")
 *  - bookingGuestId: UUID nullable (if folio is assigned to a specific BookingGuest)
 *  - status enum (OPEN/CLOSED)
 *  - createdAt/updatedAt handled by Auditable -> extend Auditable
 *  - totalAmount computed or stored (BigDecimal) - prefer compute in query or maintain by service
 *  - soft-delete not required; closing is a state change
 * Behavior:
 *  - No heavy logic inside entity.
 *  - Provide equals/hashCode only on id.
 *
 * File: billing/entity/Folio.java
 */
package com.resortmanagement.system.billing.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.booking.entity.BookingGuest;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "folio")
    public class Folio extends Auditable {

        @Id
        @GeneratedValue(generator="UUID")
        @UuidGenerator
        @Column(name = "folio_id", updatable = false, nullable = false)
        private UUID id;

        @Column(name = "folio_number", nullable = false, unique = true, length = 64)
        private String folioNumber;

        @NotBlank
        @Column(name = "name", nullable = false, length = 100)
        private String name;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false, length = 20)
        private FolioStatus status = FolioStatus.OPEN;

        @Column(name = "total_amount", precision = 10, scale = 2)
        private BigDecimal totalAmount = BigDecimal.ZERO;

        // JPA Relationships - Financial record chain: Folio -> Invoice -> Payment ->
        // Refund

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reservation_id")
        private Reservation reservation;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "booking_guest_id")
        private BookingGuest bookingGuest; 

        @OneToMany(mappedBy = "folio", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
        private List<Invoice> invoices = new ArrayList<>();

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Folio))
                return false;
            Folio folio = (Folio) o;
            return id != null && id.equals(folio.id);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hashCode(id);
        }
    }