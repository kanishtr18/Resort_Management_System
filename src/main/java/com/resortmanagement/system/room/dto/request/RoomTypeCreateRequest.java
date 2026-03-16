package com.resortmanagement.system.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double baseRate;

    private String bedType;

    private Integer areaSqFt;

    private Integer maxOccupancy;

    private String amenitiesSummary;

    private Integer totalKeys;
}
