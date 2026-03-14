package com.resortmanagement.system.booking.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.resortmanagement.system.booking.dto.request.ReservationCreateRequest;
import com.resortmanagement.system.booking.dto.request.ReservationUpdateRequest;
import com.resortmanagement.system.booking.dto.response.ReservationDetailResponse;
import com.resortmanagement.system.booking.dto.response.ReservationResponse;
import com.resortmanagement.system.booking.entity.Reservation;

public class ReservationMapper {

    public static Reservation toEntity(ReservationCreateRequest req) {
        if (req == null)
            return null;

        Reservation entity = new Reservation();
        entity.setStartDate(req.getStartDate());
        entity.setEndDate(req.getEndDate());
        entity.setNumGuests(req.getNumGuests());
        entity.setBookingSourceId(req.getBookingSourceId());
        entity.setIsPackageBooking(req.getIsPackageBooking());
        return entity;
    }

    public static void updateEntity(Reservation entity, ReservationUpdateRequest req) {
        if (req == null || entity == null)
            return;

        entity.setStartDate(req.getStartDate());
        entity.setEndDate(req.getEndDate());
        entity.setNumGuests(req.getNumGuests());
        entity.setStatus(req.getStatus());
        // bookingguest update, addon, services update are handled in services.
    }

    public static ReservationResponse toResponse(Reservation entity) {
        if (entity == null)
            return null;

        ReservationResponse res = new ReservationResponse();
        res.setId(entity.getId());
        res.setGuestId(entity.getGuest() != null ? entity.getGuest().getId() : null);
        res.setStartDate(entity.getStartDate());
        res.setEndDate(entity.getEndDate());
        res.setNumGuests(entity.getNumGuests());
        res.setStatus(entity.getStatus());
        return res;
    }

    public static ReservationDetailResponse toDetailResponse(Reservation entity) {
        if (entity == null)
            return null;

        ReservationDetailResponse res = new ReservationDetailResponse();
        res.setId(entity.getId());
        res.setStartDate(entity.getStartDate());
        res.setEndDate(entity.getEndDate());
        res.setNumGuests(entity.getNumGuests());
        res.setStatus(entity.getStatus());

        res.setGuests(
                entity.getBookingGuests() == null
                        ? Collections.emptyList()
                        : entity.getBookingGuests()
                                .stream()
                                .map(BookingGuestMapper::toResponse)
                                .collect(Collectors.toList()));

        res.setRoomAssignments(
                entity.getAssignedRooms() == null
                        ? Collections.emptyList()
                        : entity.getAssignedRooms()
                                .stream()
                                .map(ReservationRoomAssignmentMapper::toResponse)
                                .collect(Collectors.toList()));

        res.setDailyRates(
                entity.getDailyRates()
                        .stream()
                        .map(ReservationDailyRateMapper::toResponse)
                        .collect(Collectors.toList()));

        res.setAddOns(
                entity.getAddOns() == null
                        ? Collections.emptyList()
                        : entity.getAddOns()
                                .stream()
                                .map(ReservationAddOnMapper::toResponse)
                                .collect(Collectors.toList()));

        res.setServices(
                entity.getServiceBookings() == null
                        ? Collections.emptyList()
                        : entity.getServiceBookings()
                                .stream()
                                .map(ReservationServiceBookingMapper::toResponse)
                                .collect(Collectors.toList()));

        return res;
    }

}
