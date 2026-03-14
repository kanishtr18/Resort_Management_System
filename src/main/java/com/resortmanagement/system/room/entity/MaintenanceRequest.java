/*
MaintenanceRequest.java
Purpose:
 - Track maintenance issues for rooms/facilities.
Fields:
 - id UUID
 - roomOrFacilityId UUID
 - reportedBy (guestId or employeeId)
 - description String
 - severity enum
 - status enum (REPORTED, IN_PROGRESS, RESOLVED, CLOSED)
 - resolvedAt Instant
 - extends Auditable
Notes:
 - Create RoomBlock when appropriate to prevent booking.
File: room/entity/MaintenanceRequest.java
*/
package com.resortmanagement.system.room.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.resortmanagement.system.booking.entity.BookingGuest;
import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.room.enums.MaintenanceSeverity;
import com.resortmanagement.system.room.enums.MaintenanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "maintenance_requests")
@Getter
@Setter
public class MaintenanceRequest extends AuditableSoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "room_block_id")
    private RoomBlock roomBlock;

    @Column(name = "room_or_facility_id", nullable = false)
    private UUID roomOrFacilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_or_facility_id", insertable = false, updatable = false)
    private Room room;

    private String description;

    @Enumerated(EnumType.STRING)
    private MaintenanceSeverity severity;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private BookingGuest reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private Employee assignedStaff;
}
