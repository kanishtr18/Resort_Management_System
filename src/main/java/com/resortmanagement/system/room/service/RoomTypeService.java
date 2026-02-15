package com.resortmanagement.system.room.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.resortmanagement.system.room.dto.request.RoomTypeCreateRequest;
import com.resortmanagement.system.room.dto.request.RoomTypeUpdateRequest;
import com.resortmanagement.system.room.dto.response.RoomTypeResponse;
import com.resortmanagement.system.room.entity.RoomType;
import com.resortmanagement.system.room.mapper.RoomTypeMapper;
import com.resortmanagement.system.room.repository.RoomTypeRepository;

@Service
public class RoomTypeService {

    private final RoomTypeRepository repository;

    public RoomTypeService(RoomTypeRepository repository) {
        this.repository = repository;
    }

    public RoomTypeResponse create(RoomTypeCreateRequest request) {
        RoomType entity = RoomTypeMapper.toEntity(request);
        RoomType saved = repository.save(entity);
        return RoomTypeMapper.toResponse(saved);
    }

    public List<RoomTypeResponse> getAll() {
        return repository.findByDeletedFalse()
                .stream()
                .map(RoomTypeMapper::toResponse)
                .toList();
    }

    public RoomTypeResponse getById(UUID id) {
        RoomType entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        return RoomTypeMapper.toResponse(entity);
    }

    public RoomType getRoomType(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
    }

    public RoomTypeResponse update(UUID id, RoomTypeUpdateRequest request) {
        RoomType entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        RoomTypeMapper.updateEntity(request, entity);
        RoomType saved = repository.save(entity);
        return RoomTypeMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        RoomType entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        entity.setDeleted(true);
        repository.save(entity);
    }
}
