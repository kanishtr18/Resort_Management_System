package com.resortmanagement.system.room.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.room.entity.MaintenanceRequest;
import com.resortmanagement.system.room.enums.MaintenanceStatus;

@Repository
public interface MaintenanceRequestRepository extends SoftDeleteRepository<MaintenanceRequest, UUID> {
    
    List<MaintenanceRequest> findByDeletedFalseAndStatusNot(MaintenanceStatus status);
}
