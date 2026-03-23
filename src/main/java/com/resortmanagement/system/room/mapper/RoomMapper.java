package com.resortmanagement.system.room.mapper;

import java.util.List;
import java.util.UUID;

import com.resortmanagement.system.room.dto.response.RoomResponse;
import com.resortmanagement.system.room.entity.MaintenanceRequest;
import com.resortmanagement.system.room.entity.Room;
import com.resortmanagement.system.room.entity.RoomAmenity;
import com.resortmanagement.system.room.entity.RoomBlock;

public class RoomMapper {

    private RoomMapper() {} // prevent instantiation

    public static RoomResponse toResponse(Room room) {

        if (room == null) return null;

        RoomResponse res = new RoomResponse();

        res.setId(room.getId());
        res.setRoomNumber(room.getRoomNumber());
        res.setFloor(room.getFloor());
        res.setStatus(room.getStatus());
        res.setDescription(room.getDescription());
        res.setMaxOccupancy(room.getMaxOccupancy());

        res.setRoomTypeId(
                room.getRoomType() != null ? room.getRoomType().getId() : null
        );

        res.setRoomTypeName(
        room.getRoomType() != null ? room.getRoomType().getName() : "Standard"
);
        res.setAmenityIds(mapAmenities(room.getRoomAmenities()));
        res.setRoomBlockIds(mapRoomBlocks(room.getRoomBlocks()));
        res.setMaintenanceIds(mapMaintenance(room.getMaintenanceRequests()));

        return res;
    }

    public static List<UUID> mapAmenities(List<RoomAmenity> list) {
        return list == null ? List.of()
                : list.stream().map(RoomAmenity::getId).toList();
    }

    public static List<UUID> mapRoomBlocks(List<RoomBlock> list) {
        return list == null ? List.of()
                : list.stream().map(RoomBlock::getId).toList();
    }

    public static List<UUID> mapMaintenance(List<MaintenanceRequest> list) {
        return list == null ? List.of()
                : list.stream().map(MaintenanceRequest::getId).toList();
    }
}
