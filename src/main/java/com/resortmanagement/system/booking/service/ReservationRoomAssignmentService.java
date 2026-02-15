package com.resortmanagement.system.booking.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.booking.dto.request.ReservationRoomAssignmentRequest;
import com.resortmanagement.system.booking.dto.response.ReservationRoomAssignmentResponse;
import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.booking.entity.ReservationRoomAssignment;
import com.resortmanagement.system.booking.mapper.ReservationRoomAssignmentMapper;
import com.resortmanagement.system.booking.repository.ReservationRepository;
import com.resortmanagement.system.booking.repository.ReservationRoomAssignmentRepository;
import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.room.enums.RoomStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationRoomAssignmentService {

    private final ReservationRoomAssignmentRepository repository;
    private final ReservationRepository reservationRepository;

    public ReservationRoomAssignmentResponse assignRoom(
        UUID reservationId,
        ReservationRoomAssignmentRequest request
    ) {

        Reservation reservation = reservationRepository.findByIdAndDeletedFalse(reservationId)
            .orElseThrow(() -> new ApplicationException("Reservation not found"));

        ReservationRoomAssignment assignment =
            ReservationRoomAssignmentMapper.toEntity(request);
        assignment.setReservation(reservation);

        repository.save(assignment);
        return ReservationRoomAssignmentMapper.toResponse(assignment);
    }

    public void unassignRoom(UUID assignmentId) {
        ReservationRoomAssignment assignment = repository.findByIdAndDeletedFalse(assignmentId)
            .orElseThrow(() -> new ApplicationException("Room assignment not found"));
        assignment.getRoom().setStatus(RoomStatus.AVAILABLE);
        repository.save(assignment);
    }
}
