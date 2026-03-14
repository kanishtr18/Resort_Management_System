package com.resortmanagement.system.booking.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.booking.entity.ReservationAddOn;
import com.resortmanagement.system.common.repository.SoftDeleteRepository;

@Repository
public interface ReservationAddOnRepository extends SoftDeleteRepository<ReservationAddOn, UUID> {
    // add custom queries if needed
}
