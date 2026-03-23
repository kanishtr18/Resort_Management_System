package com.resortmanagement.system.booking.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.booking.dto.response.ReservationDailyRateResponse;
import com.resortmanagement.system.booking.mapper.ReservationDailyRateMapper;
import com.resortmanagement.system.booking.repository.ReservationDailyRateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationDailyRateService {

    private final ReservationDailyRateRepository repository;

    public List<ReservationDailyRateResponse> getRates(UUID reservationId) {
        return repository.findByReservationId(reservationId)
            .stream()
            .map(ReservationDailyRateMapper::toResponse)
            .toList();
    }
}