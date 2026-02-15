package com.resortmanagement.system.booking.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.room.entity.RoomType;

@Repository
public interface ReservationRepository extends SoftDeleteRepository<Reservation, UUID> {   
    @Query("SELECT COUNT(r) FROM Reservation r " +
           "WHERE r.ratePlan.roomTypeId = :roomTypeId " +
           "AND r.deleted = false " +
           "AND r.status <> 'CANCELLED' " +
           "AND (r.startDate < :endDate AND r.endDate > :startDate)")
    long countOverlappingReservations(@org.springframework.data.repository.query.Param("roomTypeId") RoomType roomTypeId, 
                                      @org.springframework.data.repository.query.Param("startDate") LocalDate startDate, 
                                      @org.springframework.data.repository.query.Param("endDate") LocalDate endDate);

}
