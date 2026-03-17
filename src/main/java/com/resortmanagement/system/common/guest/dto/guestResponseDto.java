package com.resortmanagement.system.common.guest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.resortmanagement.system.common.enums.GuestType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class guestResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private GuestType guestType;
    private String phone;
    private String address;
    private LocalDate dob;
    private Integer age;
    private UUID loyaltyId;
    private String loyaltyTier;
    private BigDecimal pointsBalance;
}
