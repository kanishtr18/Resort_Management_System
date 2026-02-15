package com.resortmanagement.system.booking.mapper;

import com.resortmanagement.system.booking.dto.request.ReservationRoomAssignmentRequest;
import com.resortmanagement.system.booking.dto.response.ReservationRoomAssignmentResponse;
import com.resortmanagement.system.booking.entity.ReservationRoomAssignment;

public class ReservationRoomAssignmentMapper {

    public static ReservationRoomAssignment toEntity(ReservationRoomAssignmentRequest req) {
        if (req == null) return null;

        ReservationRoomAssignment entity = new ReservationRoomAssignment();
        entity.setCheckInDate(req.getCheckInDate());
        entity.setCheckOutDate(req.getCheckOutDate());
        entity.setStatus(req.getStatus());
        return entity;
    }

    public static ReservationRoomAssignmentResponse toResponse(ReservationRoomAssignment entity) {
        if (entity == null) return null;

        ReservationRoomAssignmentResponse res = new ReservationRoomAssignmentResponse();
        res.setId(entity.getId());
        res.setRoomId(entity.getRoom() != null ? entity.getRoom().getId() : null);
        res.setCheckInDate(entity.getCheckInDate());
        res.setCheckOutDate(entity.getCheckOutDate());
        res.setStatus(entity.getStatus());
        return res;
    }
}
