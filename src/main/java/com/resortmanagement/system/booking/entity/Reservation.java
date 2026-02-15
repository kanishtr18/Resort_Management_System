/*
Reservation.java
Purpose:
 - Core reservation entity that locks the booking.
Fields & annotations:
 - @Entity @Table("reservation")
 - id: UUID
 - guest: Guest (@ManyToOne lazy) -> reference to common.entity.Guest
 - roomType: RoomType (@ManyToOne lazy) -> REQUIRED at booking confirmation
 - bookingSourceId: UUID (referrer or OTAs)
 - startDate, endDate: LocalDate (hotel local timezone; convert to UTC only for timestamps)
 - numGuests: int
 - status: ReservationStatus (ENUM) with @Enumerated(STRING)
 - assignedRoom: Room (nullable @ManyToOne)
 - ratePlan: RatePlan (@ManyToOne)
 - createdBy/updatedBy handled by Auditable -> extend Auditable
 - soft-delete fields (deleted, deletedAt) if you prefer to soft delete reservations
Constraints & behavior:
 - Add DB constraints (startDate <= endDate).
 - Enforce reservation.room_type_id is immutable after confirmation (service-level validation).
 - No heavy logic in entity.
Important concurrency:
 - availability checks in ReservationService should lock on roomtype or use Redis to avoid overbooking.
File: booking/entity/Reservation.java
*/

package com.resortmanagement.system.booking.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.enums.ReservationStatus;
import com.resortmanagement.system.common.guest.Guest;
import com.resortmanagement.system.marketing.entity.Package;
import com.resortmanagement.system.pricing.entity.RatePlan;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "reservation")
public class Reservation extends AuditableSoftDeletable {
    @Id
    @UuidGenerator
    @Column(name = "reservation_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    private UUID bookingSourceId;
    private Boolean isPackageBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private Package packageEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numGuests;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationDailyRate> dailyRates = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationServiceBooking> serviceBookings = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationAddOn> addOns = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookingGuest> bookingGuests = new HashSet<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationRoomAssignment> assignedRooms = new ArrayList<>();
}