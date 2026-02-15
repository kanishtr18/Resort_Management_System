package com.resortmanagement.system.booking.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.booking.dto.request.BookingGuestRequest;
import com.resortmanagement.system.booking.dto.response.BookingGuestResponse;
import com.resortmanagement.system.booking.entity.BookingGuest;
import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.booking.mapper.BookingGuestMapper;
import com.resortmanagement.system.booking.repository.BookingGuestRepository;
import com.resortmanagement.system.booking.repository.ReservationRepository;
import com.resortmanagement.system.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingGuestService {

    private final BookingGuestRepository bookingGuestRepository;
    private final ReservationRepository reservationRepository;

    public BookingGuestResponse addGuest(UUID reservationId, BookingGuestRequest request) {

        Reservation reservation = reservationRepository.findByIdAndDeletedFalse(reservationId)
            .orElseThrow(() -> new ApplicationException("Reservation not found"));

        BookingGuest guest = BookingGuestMapper.toEntity(request);
        guest.setReservation(reservation);

        bookingGuestRepository.save(guest);
        return BookingGuestMapper.toResponse(guest);
    }

    public void removeGuest(UUID id) {
        bookingGuestRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ApplicationException("Booking guest not found"));

        bookingGuestRepository.softDeleteById(id, Instant.now());
    }
}