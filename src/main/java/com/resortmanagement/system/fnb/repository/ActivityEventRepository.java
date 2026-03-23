package com.resortmanagement.system.fnb.repository;

import com.resortmanagement.system.fnb.entity.ActivityEvent;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;

@Repository
public interface ActivityEventRepository extends SoftDeleteRepository<ActivityEvent, UUID> {

    Optional<ActivityEvent> findAllById(Long id);

    List<ActivityEvent> findByDeletedFalse();

}
