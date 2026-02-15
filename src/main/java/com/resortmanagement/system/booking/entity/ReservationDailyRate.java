/*
ReservationDailyRate.java
Purpose:
 - Represents authoritative nightly price snapshot for a reservation.
Fields:
 - id: UUID
 - reservation: Reservation (ManyToOne)
 - date: LocalDate
 - amount: BigDecimal (final nightly charge)
 - ratePlanId: UUID (FK, optional)
 - rateHistoryId: UUID (FK, optional)
 - createdAt handled by Auditable (extend Auditable)
Constraints:
 - Unique constraint (reservation, date)
 - Immutable once written (service must enforce)
Behavior:
 - Only created by ReservationService during booking flow.

File: booking/entity/ReservationDailyRate.java
*/

package com.resortmanagement.system.booking.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.common.audit.Auditable;
import com.resortmanagement.system.pricing.entity.RatePlan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "reservation_daily_rate")
public class ReservationDailyRate extends Auditable {

    @Id
    @UuidGenerator
    @Column(name = "reservation_daily_rate_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private LocalDate date;

    @Column(name="is_package_rate")
    private Boolean isPackageRate;
    
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

}