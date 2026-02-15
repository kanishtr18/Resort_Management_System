package com.resortmanagement.system.pricing.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatePlanCreateRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double basePrice;

    private Boolean refundable;

    private Integer minStayNights;
    private Integer maxStayNights;

    private UUID roomTypeId;

    private LocalDate validFrom;
    private LocalDate validTo;
}
