package com.resortmanagement.system.booking.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.booking.entity.BookingGuest;
import com.resortmanagement.system.common.repository.SoftDeleteRepository;

@Repository
public interface BookingGuestRepository extends SoftDeleteRepository<BookingGuest, UUID> {
    // add custom queries if needed
}
