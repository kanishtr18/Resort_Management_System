/*
BookingGuest.java
Purpose:
 - Junction entity for multiple guests per reservation.
Fields:
 - id: UUID
 - reservation: Reservation (@ManyToOne, LAZY)
 - guest: Guest (reference to common.entity.Guest)
 - isPrimary: boolean
 - age: Integer
 - specialNeeds: String
 - extends Auditable (tracking who added / changed occupant info)
Notes:
 - Validate fields (age >= 0).
 - Do NOT duplicate guest personal data here; reference Guest entity.
File: booking/entity/BookingGuest.java
*/

package com.resortmanagement.system.booking.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.enums.GuestType;
import com.resortmanagement.system.common.guest.Guest;

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
@Table(name = "booking_guest")
public class BookingGuest extends AuditableSoftDeletable {
    @Id
    
    @UuidGenerator
    @Column(name = "booking_guest_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /** Indicates if this guest is the primary contact for the reservation */
    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @Column(name = "guest_type", nullable = false)
    private GuestType guestType;

    @Column(name = "age")
    private Integer age;

    /**
     * Any special needs or requests for this guests (e.g., dietary restrictions,
     * accessibility requirements)
     */
    @Column(name = "special_needs", length = 500)
    private String specialNeeds;
}
