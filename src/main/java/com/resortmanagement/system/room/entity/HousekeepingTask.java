/*
HousekeepingTask.java
Purpose:
 - Housekeeping work orders for rooms.
Fields:
 - id UUID
 - roomId UUID
 - staffId UUID
 - scheduledAt Instant
 - priority String
 - status enum (PENDING, IN_PROGRESS, DONE)
 - notes String
 - extends Auditable (track who scheduled/completed)
File: room/entity/HousekeepingTask.java
*/
package com.resortmanagement.system.room.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.room.enums.HousekeepingPriority;
import com.resortmanagement.system.room.enums.HousekeepingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "housekeeping_tasks")
@Getter
@Setter
public class HousekeepingTask extends AuditableSoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Employee staff;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HousekeepingPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HousekeepingStatus status;

    @Column(length = 500)
    private String notes;
}
