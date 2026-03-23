package com.resortmanagement.system.hr.repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.hr.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role, UUID> {
    boolean existsByName(String name);

    java.util.Optional<Role> findByName(String name);

    java.util.Optional<Role> findByNameIgnoreCase(String name);

    org.springframework.data.domain.Page<Role> findByDeletedFalse(org.springframework.data.domain.Pageable pageable);
}
