package com.resortmanagement.system.fnb.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.resortmanagement.system.common.enums.ActivityEventStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

// import lombok.Data; // Removing Lombok usage

@Getter
@Setter
public class ActivityEventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start time is required")
    // @Future(message = "Start time must be in the future")
    private Instant startTime;

    @NotNull(message = "End time is required")
    // @Future(message = "End time must be in the future")
    private Instant endTime;

    @PositiveOrZero(message = "Capacity must be positive or zero")
    private Integer capacity;

    @NotNull(message = "Instructor is required")
    private UUID instructor;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    private BigDecimal price;

    @NotNull(message = "Status is required")
    private ActivityEventStatus status;
}
