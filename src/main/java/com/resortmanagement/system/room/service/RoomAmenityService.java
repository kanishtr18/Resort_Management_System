package com.resortmanagement.system.room.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.room.dto.request.RoomAmenityCreateRequest;
import com.resortmanagement.system.room.dto.response.RoomAmenityResponse;
import com.resortmanagement.system.room.entity.Amenity;
import com.resortmanagement.system.room.entity.Room;
import com.resortmanagement.system.room.entity.RoomAmenity;
import com.resortmanagement.system.room.mapper.RoomAmenityMapper;
import com.resortmanagement.system.room.repository.AmenityRepository;
import com.resortmanagement.system.room.repository.RoomAmenityRepository;
import com.resortmanagement.system.room.repository.RoomRepository;

@Service
public class RoomAmenityService {

    private final RoomAmenityRepository roomAmenityRepo;
    private final RoomRepository roomRepo;
    private final AmenityRepository amenityRepo;

    public RoomAmenityService(
            RoomAmenityRepository roomAmenityRepo,
            RoomRepository roomRepo,
            AmenityRepository amenityRepo) {

        this.roomAmenityRepo = roomAmenityRepo;
        this.roomRepo = roomRepo;
        this.amenityRepo = amenityRepo;
    }

    public List<RoomAmenityResponse> getAll() {
        return roomAmenityRepo.findAll().stream().map(RoomAmenityMapper::toResponse).toList();
    }
    
    public RoomAmenityResponse create(RoomAmenityCreateRequest request) {

        Room room = roomRepo.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Amenity amenity = amenityRepo.findById(request.getAmenityId())
                .orElseThrow(() -> new RuntimeException("Amenity not found"));

        RoomAmenity entity = new RoomAmenity();
        entity.setRoom(room);
        entity.setAmenity(amenity);
        entity.setComplimentary(request.getComplimentary());

        return RoomAmenityMapper.toResponse(roomAmenityRepo.save(entity));
    }

    public void delete(UUID id) {
        roomAmenityRepo.deleteById(id);
    }
}
