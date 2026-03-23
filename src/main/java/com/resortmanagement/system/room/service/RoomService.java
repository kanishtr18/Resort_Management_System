package com.resortmanagement.system.room.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.room.dto.request.RoomCreateRequest;
import com.resortmanagement.system.room.dto.request.RoomUpdateRequest;
import com.resortmanagement.system.room.dto.response.RoomResponse;
import com.resortmanagement.system.room.entity.Room;
import com.resortmanagement.system.room.entity.RoomType;
import com.resortmanagement.system.room.mapper.RoomMapper;
import com.resortmanagement.system.room.repository.RoomRepository;
import com.resortmanagement.system.room.repository.RoomTypeRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomService(RoomRepository roomRepository,
                       RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    // CREATE ROOM
    public RoomResponse create(RoomCreateRequest request) {
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());
        room.setStatus(request.getStatus());
        room.setDescription(request.getDescription());
        room.setMaxOccupancy(request.getMaxOccupancy());
        room.setRoomType(roomType);

        Room saved = roomRepository.save(room);
        return RoomMapper.toResponse(saved);
    }

    // GET ALL ROOMS
    public List<RoomResponse> getAll() {
        return roomRepository.findByDeletedFalse()
                .stream()
                .map(RoomMapper::toResponse)
                .toList();
    }

    // GET BY ID
   // Fix: use findByIdAndDeletedFalse instead of findById
    public RoomResponse getById(UUID id) {
        Room room = roomRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return RoomMapper.toResponse(room);
    }

    // UPDATE ROOM
    public RoomResponse updateRoom(UUID id, RoomUpdateRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (request.getRoomNumber() != null)
            room.setRoomNumber(request.getRoomNumber());

        if (request.getFloor() != null)
            room.setFloor(request.getFloor());

        if (request.getStatus() != null)
            room.setStatus(request.getStatus());

        if (request.getDescription() != null)
            room.setDescription(request.getDescription());

        if (request.getMaxOccupancy() != null)
            room.setMaxOccupancy(request.getMaxOccupancy());

        // FIX: Update RoomType if provided
        if (request.getRoomTypeId() != null) {
            RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                    .orElseThrow(() -> new RuntimeException("RoomType not found"));
            room.setRoomType(roomType);
        }

        Room saved = roomRepository.save(room);
        return RoomMapper.toResponse(saved);
    }

    // SOFT DELETE ROOM
    public void deleteRoom(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setDeleted(true);   // soft delete
        roomRepository.save(room);
    }
}
