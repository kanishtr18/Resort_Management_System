package com.resortmanagement.system.room.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import com.resortmanagement.system.room.enums.HousekeepingPriority;
import com.resortmanagement.system.room.enums.HousekeepingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HousekeepingTaskUpdateRequest {

    private UUID staffId;
    private LocalDateTime scheduledAt;
    private HousekeepingPriority priority;
    private HousekeepingStatus status;
    private String notes;
}
