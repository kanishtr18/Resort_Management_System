package com.resortmanagement.system.support.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import com.resortmanagement.system.support.dto.response.HelpTicketResponse;
import com.resortmanagement.system.support.entity.HelpTicket;

@Component
public class HelpTicketMapper {

    public HelpTicketResponse toResponse(HelpTicket entity) {

        HelpTicketResponse res = new HelpTicketResponse();

        res.setId(entity.getId());
        res.setTicketNumber(entity.getTicketNumber());
        res.setCategory(entity.getCategory());
        res.setDescription(entity.getDescription());
        res.setPriority(entity.getPriority());
        res.setStatus(entity.getStatus());
        res.setAssignedTo(entity.getAssignedTo() != null ? entity.getAssignedTo().getId() : null);

        if (entity.getGuest() != null)
            res.setGuestId(entity.getGuest().getId());

        if (entity.getReservation() != null)
            res.setReservationId(entity.getReservation().getId());

        if (entity.getCreatedAt() != null) {
            res.setCreatedAt(
                LocalDateTime.ofInstant(
                    entity.getCreatedAt(),
                    ZoneId.systemDefault()
                )
            );
        }

        return res;
    }
}
