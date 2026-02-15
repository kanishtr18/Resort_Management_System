package com.resortmanagement.system.booking.mapper;

import com.resortmanagement.system.booking.dto.response.ReservationDailyRateResponse;
import com.resortmanagement.system.booking.entity.ReservationDailyRate;

public class ReservationDailyRateMapper {

    public static ReservationDailyRateResponse toResponse(ReservationDailyRate entity) {
        if (entity == null) return null;

        ReservationDailyRateResponse res = new ReservationDailyRateResponse();
        res.setId(entity.getId());
        res.setDate(entity.getDate());
        res.setAmount(entity.getAmount());
        res.setIsPackageRate(entity.getIsPackageRate());
        res.setRatePlanId(
            entity.getRatePlan() != null ? entity.getRatePlan().getId() : null
        );
        return res;
    }
}
