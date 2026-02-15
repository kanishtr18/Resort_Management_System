package com.resortmanagement.system.booking.mapper;

import com.resortmanagement.system.booking.dto.request.BookingGuestRequest;
import com.resortmanagement.system.booking.dto.response.BookingGuestResponse;
import com.resortmanagement.system.booking.entity.BookingGuest;

public class BookingGuestMapper {
    public static BookingGuest toEntity(BookingGuestRequest req) {
        if (req == null) return null;

        BookingGuest entity = new BookingGuest();
        entity.setPrimary(req.getIsPrimary());
        entity.setGuestType(req.getGuestType());
        entity.setAge(req.getAge());
        entity.setSpecialNeeds(req.getSpecialNeeds());
        return entity;
    }

    public static BookingGuestResponse toResponse(BookingGuest entity) {
        if (entity == null) return null;

        BookingGuestResponse res = new BookingGuestResponse();
        res.setId(entity.getId());
        res.setGuestId(entity.getGuest() != null ? entity.getGuest().getId() : null);
        res.setReservationId(entity.getReservation() != null ? entity.getReservation().getId() : null);
        res.setIsPrimary(entity.isPrimary());
        res.setGuestType(entity.getGuestType());
        res.setAge(entity.getAge());
        res.setSpecialNeeds(entity.getSpecialNeeds());
        return res;
    }
}
