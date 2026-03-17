package com.resortmanagement.system.hr.repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.hr.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role, UUID> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);

    Page<Role> findByDeletedFalse(Pageable pageable);
}
