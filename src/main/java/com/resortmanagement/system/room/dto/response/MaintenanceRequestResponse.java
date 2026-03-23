package com.resortmanagement.system.room.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.resortmanagement.system.room.enums.MaintenanceSeverity;
import com.resortmanagement.system.room.enums.MaintenanceStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaintenanceRequestResponse {

    private UUID id;
    private UUID roomOrFacilityId;
    private UUID roomBlockId;

    // Fix: separate fields instead of single reportedById
    private UUID reportedByEmployeeId;
    private UUID reportedByGuestId;
    private UUID assignedStaffId;

    private String description;
    private MaintenanceSeverity severity;
    private MaintenanceStatus status;
    private LocalDateTime resolvedAt;
}