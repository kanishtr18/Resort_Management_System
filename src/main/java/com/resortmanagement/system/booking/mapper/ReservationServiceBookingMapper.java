package com.resortmanagement.system.booking.mapper;

import com.resortmanagement.system.booking.dto.request.ReservationServiceBookingRequest;
import com.resortmanagement.system.booking.dto.response.ReservationServiceBookingResponse;
import com.resortmanagement.system.booking.entity.ReservationServiceBooking;

public class ReservationServiceBookingMapper {
    
    public static ReservationServiceBooking toEntity(ReservationServiceBookingRequest req) {
        if (req == null) return null;

        ReservationServiceBooking entity = new ReservationServiceBooking();
        entity.setScheduledAt(req.getScheduledAt());
        entity.setQuantity(req.getQuantity());
        entity.setPrice(req.getPrice());
        entity.setStatus(req.getStatus());
        entity.setIs_included_in_package(req.getIsIncludedInPackage());
        return entity;
    }

    public static ReservationServiceBookingResponse toResponse(ReservationServiceBooking entity) {
        if (entity == null) return null;

        ReservationServiceBookingResponse res = new ReservationServiceBookingResponse();
        res.setId(entity.getId());
        res.setServiceItemId(
            entity.getServiceItem() != null ? entity.getServiceItem().getId() : null
        );
        res.setScheduledAt(entity.getScheduledAt());
        res.setQuantity(entity.getQuantity());
        res.setPrice(entity.getPrice());
        res.setStatus(entity.getStatus());
        res.setIncludedInPackage(entity.getIs_included_in_package());
        return res;
    }

}
