package com.resortmanagement.system.booking.dto.request;

import java.util.UUID;

import com.resortmanagement.system.common.enums.GuestType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingGuestRequest{
    private UUID guestId;
    private GuestType guestType;
    private Integer age;
    private Boolean isPrimary;
    private String specialNeeds;
}
