package com.resortmanagement.system.booking.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resortmanagement.system.booking.entity.ReservationDailyRate;

@Repository
public interface ReservationDailyRateRepository extends JpaRepository<ReservationDailyRate, UUID> {
    List<ReservationDailyRate> findByReservationId(UUID reservationId);
}
