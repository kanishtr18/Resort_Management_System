package com.resortmanagement.system.booking.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.booking.entity.ReservationServiceBooking;

@Repository
public interface ReservationServiceBookingRepository extends SoftDeleteRepository<ReservationServiceBooking, UUID> {
    // add custom queries if needed
}
