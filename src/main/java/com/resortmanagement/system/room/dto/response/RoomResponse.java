package com.resortmanagement.system.room.dto.response;

import java.util.List;
import java.util.UUID;

import com.resortmanagement.system.room.enums.RoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {

    private UUID id;

    private String roomNumber;

    private String floor;

    private RoomStatus status;

    private String description;

    private Integer maxOccupancy;

    private UUID roomTypeId;

    private String roomTypeName;

    private List<UUID> amenityIds;

    private List<UUID> roomBlockIds;

    private List<UUID> maintenanceIds;
}
