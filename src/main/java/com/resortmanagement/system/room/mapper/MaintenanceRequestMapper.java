package com.resortmanagement.system.room.mapper;

import java.util.List;

import com.resortmanagement.system.room.dto.response.MaintenanceRequestResponse;
import com.resortmanagement.system.room.entity.MaintenanceRequest;

public class MaintenanceRequestMapper {

    private MaintenanceRequestMapper() {}

    public static MaintenanceRequestResponse toResponse(MaintenanceRequest entity) {
        if (entity == null) return null;

        MaintenanceRequestResponse res = new MaintenanceRequestResponse();
        res.setId(entity.getId());
        res.setRoomOrFacilityId(entity.getRoomOrFacilityId());
        res.setRoomBlockId(entity.getRoomBlock() != null ? entity.getRoomBlock().getId() : null);

        // Fix: map both reporter fields
        res.setReportedByEmployeeId(
            entity.getReportedByEmployee() != null ? entity.getReportedByEmployee().getId() : null
        );
        res.setReportedByGuestId(
            entity.getReportedByGuest() != null ? entity.getReportedByGuest().getId() : null
        );

        res.setAssignedStaffId(
            entity.getAssignedStaff() != null ? entity.getAssignedStaff().getId() : null
        );
        res.setDescription(entity.getDescription());
        res.setSeverity(entity.getSeverity());
        res.setStatus(entity.getStatus());
        res.setResolvedAt(entity.getResolvedAt());
        return res;
    }

    public static List<MaintenanceRequestResponse> toResponseList(List<MaintenanceRequest> list) {
        return list.stream().map(MaintenanceRequestMapper::toResponse).toList();
    }
}