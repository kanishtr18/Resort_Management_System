package com.resortmanagement.system.booking.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.booking.dto.request.ReservationAddOnRequest;
import com.resortmanagement.system.booking.dto.response.ReservationAddOnResponse;
import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.booking.entity.ReservationAddOn;
import com.resortmanagement.system.booking.mapper.ReservationAddOnMapper;
import com.resortmanagement.system.booking.repository.ReservationAddOnRepository;
import com.resortmanagement.system.booking.repository.ReservationRepository;
import com.resortmanagement.system.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationAddOnService {

    private final ReservationAddOnRepository repository;
    private final ReservationRepository reservationRepository;

    public ReservationAddOnResponse addAddOn(UUID reservationId, ReservationAddOnRequest request) {

        Reservation reservation = reservationRepository.findByIdAndDeletedFalse(reservationId)
            .orElseThrow(() -> new ApplicationException("Reservation not found"));

        ReservationAddOn addOn = ReservationAddOnMapper.toEntity(request);
        addOn.setReservation(reservation);

        repository.save(addOn);
        return ReservationAddOnMapper.toResponse(addOn);
    }

    public void removeAddOn(UUID id) {
        repository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ApplicationException("Reservation add-on not found"));

        repository.softDeleteById(id, Instant.now());
    }
}
