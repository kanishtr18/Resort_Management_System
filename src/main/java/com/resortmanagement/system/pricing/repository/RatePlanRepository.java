package com.resortmanagement.system.pricing.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.pricing.entity.RatePlan;

@Repository
public interface RatePlanRepository extends SoftDeleteRepository<RatePlan, UUID> {

    Optional<RatePlan> findByNameAndDeletedFalse(String name);

}

