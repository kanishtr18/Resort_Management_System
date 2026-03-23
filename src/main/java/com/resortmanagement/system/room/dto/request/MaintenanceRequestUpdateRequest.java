    package com.resortmanagement.system.room.dto.request;

    import java.time.LocalDateTime;
    import java.util.UUID;

    import com.resortmanagement.system.room.enums.MaintenanceSeverity;
    import com.resortmanagement.system.room.enums.MaintenanceStatus;

    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class MaintenanceRequestUpdateRequest {

        private UUID roomBlockId;          // optional: link to RoomBlock if needed
        private UUID assignedStaffId;      // optional: assign staff
        private MaintenanceSeverity severity; // optional: change severity
        private MaintenanceStatus status;     // optional: change status
        private LocalDateTime resolvedAt;     // optional: mark resolved
        private String description;           // optional: update description
    }
