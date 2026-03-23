package com.resortmanagement.system.room.dto.request;

import java.util.UUID;

import com.resortmanagement.system.room.enums.MaintenanceSeverity;
import com.resortmanagement.system.room.enums.MaintenanceStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaintenanceRequestCreateRequest {

    @NotNull(message = "Room or facility ID is required")
    private UUID roomOrFacilityId;

    // Fix: separate fields for employee or guest reporter
    // At least one must be provided — validated in service
    private UUID reportedByEmployeeId;
    private UUID reportedByGuestId;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Severity is required")
    private MaintenanceSeverity severity;

    @NotNull(message = "Status is required")
    private MaintenanceStatus status;
}