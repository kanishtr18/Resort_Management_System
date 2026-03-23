package com.resortmanagement.system.room.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resortmanagement.system.room.entity.HousekeepingTask;

public interface HousekeepingTaskRepository extends JpaRepository<HousekeepingTask, UUID> {

    List<HousekeepingTask> findByDeletedFalse();
    List<HousekeepingTask> findByStaffIdAndDeletedFalse(UUID staffId);
}
