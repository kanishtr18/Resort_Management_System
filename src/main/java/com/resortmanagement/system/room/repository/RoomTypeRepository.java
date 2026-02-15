package com.resortmanagement.system.room.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.room.entity.RoomType;

@Repository
public interface RoomTypeRepository extends SoftDeleteRepository<RoomType, UUID> {
}
