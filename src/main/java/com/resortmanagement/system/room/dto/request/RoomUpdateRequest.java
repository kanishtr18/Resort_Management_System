package com.resortmanagement.system.room.dto.request;

import java.util.UUID;

import com.resortmanagement.system.room.enums.RoomStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomUpdateRequest {

    @Size(max = 50, message = "Room number must be at most 50 characters")
    private String roomNumber;

    @Size(max = 20, message = "Floor must be at most 20 characters")
    private String floor;

    private RoomStatus status;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Min(value = 1, message = "Max occupancy must be at least 1")
    @jakarta.validation.constraints.Positive
    private Integer maxOccupancy;

    private UUID roomTypeId;
}
