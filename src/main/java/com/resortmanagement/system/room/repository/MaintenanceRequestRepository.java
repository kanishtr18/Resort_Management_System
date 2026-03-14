package com.resortmanagement.system.room.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.room.entity.MaintenanceRequest;

@Repository
public interface MaintenanceRequestRepository extends SoftDeleteRepository<MaintenanceRequest, UUID> {
    // add custom queries if needed
}
