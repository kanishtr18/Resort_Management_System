package com.resortmanagement.system.booking.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationCreateRequest {
        private UUID guestId;
        private UUID roomTypeId;
        private UUID ratePlanId; // Added for explicit rate plan selection
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer numGuests;
        private List<BookingGuestRequest> bookingGuests;
        private UUID bookingSourceId;
        private Boolean isPackageBooking;
        private List<AddOnSelectionRequest> addOns;
}
