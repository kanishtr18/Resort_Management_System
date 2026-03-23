package com.resortmanagement.system.room.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.room.dto.request.RoomBlockCreateRequest;
import com.resortmanagement.system.room.dto.request.RoomBlockUpdateRequest;
import com.resortmanagement.system.room.dto.response.RoomBlockResponse;
import com.resortmanagement.system.room.entity.RoomBlock;
import com.resortmanagement.system.room.mapper.RoomBlockMapper;
import com.resortmanagement.system.room.repository.MaintenanceRequestRepository;
import com.resortmanagement.system.room.repository.RoomBlockRepository;
import com.resortmanagement.system.room.repository.RoomRepository;

@Service
public class RoomBlockService {

    private final RoomBlockRepository repository;
    private final RoomRepository roomRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;


    public RoomBlockService(
        RoomBlockRepository repository,
        RoomRepository roomRepository,
        MaintenanceRequestRepository maintenanceRequestRepository) {
    this.repository = repository;
    this.roomRepository = roomRepository;
    this.maintenanceRequestRepository = maintenanceRequestRepository;
}

public RoomBlockResponse create(RoomBlockCreateRequest dto) {
    RoomBlock entity = new RoomBlock();
    entity.setStartDate(dto.getStartDate());
    entity.setEndDate(dto.getEndDate());
    entity.setReason(dto.getReason());
    entity.setStatus(dto.getStatus());

    // Fix: fetch proper entities instead of creating detached ones
    if (dto.getRoomId() != null) {
        entity.setRoom(
            roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"))
        );
    }

    if (dto.getMaintenanceRequestId() != null) {
        entity.setMaintenanceRequest(
            maintenanceRequestRepository.findById(dto.getMaintenanceRequestId())
                .orElseThrow(() -> new RuntimeException("MaintenanceRequest not found"))
        );
    }

    return RoomBlockMapper.toResponse(repository.save(entity));
}


    public List<RoomBlockResponse> getAll() {
        return RoomBlockMapper.toResponseList(repository.findByDeletedFalse());
    }

    public RoomBlockResponse getById(UUID id) {
        RoomBlock entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomBlock not found"));
        return RoomBlockMapper.toResponse(entity);
    }

    public RoomBlockResponse update(UUID id, RoomBlockUpdateRequest dto) {
        RoomBlock entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomBlock not found"));
        RoomBlockMapper.updateEntity(entity, dto);
        RoomBlock saved = repository.save(entity);
        return RoomBlockMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        RoomBlock entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomBlock not found"));
        entity.setDeleted(true);
        repository.save(entity);
    }
}
