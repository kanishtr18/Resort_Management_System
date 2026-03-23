package com.resortmanagement.system.booking.mapper;

import com.resortmanagement.system.booking.dto.request.ReservationAddOnRequest;
import com.resortmanagement.system.booking.dto.response.ReservationAddOnResponse;
import com.resortmanagement.system.booking.entity.ReservationAddOn;

public class ReservationAddOnMapper {

    private ReservationAddOnMapper() {}

    public static ReservationAddOn toEntity(ReservationAddOnRequest req) {
        if (req == null) return null;

        ReservationAddOn entity = new ReservationAddOn();
        entity.setAddOnCode(req.getAddOnCode());
        entity.setAddOnName(req.getAddOnName());
        entity.setQuantity(req.getQuantity());
        entity.setUnitPrice(req.getUnitPrice());
        entity.setTotalPrice(req.getUnitPrice() * req.getQuantity());
        entity.setStatus(req.getStatus());
        return entity;
    }

    public static ReservationAddOnResponse toResponse(ReservationAddOn entity) {
        if (entity == null) return null;

        ReservationAddOnResponse res = new ReservationAddOnResponse();
        res.setId(entity.getId());
        res.setAddOnCode(entity.getAddOnCode());
        res.setAddOnName(entity.getAddOnName());
        res.setQuantity(entity.getQuantity());
        res.setUnitPrice(entity.getUnitPrice());
        res.setTotalPrice(entity.getTotalPrice());
        res.setStatus(entity.getStatus());
        return res;
    }
}
