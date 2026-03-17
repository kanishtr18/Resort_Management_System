package com.resortmanagement.system.hr.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.hr.dto.role.RoleRequest;
import com.resortmanagement.system.hr.dto.role.RoleResponse;
import com.resortmanagement.system.hr.entity.Role;
import com.resortmanagement.system.hr.mapper.RoleMapper;
import com.resortmanagement.system.hr.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.repository = roleRepository;
        this.mapper = roleMapper;
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        return repository.findByDeletedFalse().stream()
            .map(mapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<RoleResponse> findById(UUID id) {
        return repository.findByIdAndDeletedFalse(id).map(mapper::toResponse);
    }

    public RoleResponse save(RoleRequest dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Role name is required");
        }

        Role role = mapper.toEntity(dto);
        Role saved = repository.save(role);
        return mapper.toResponse(saved);
    }

    public RoleResponse update(UUID id, RoleRequest dto) {
        return repository.findByIdAndDeletedFalse(id)
                .map(existing -> {
                    mapper.updateEntity(existing, dto);
                    return mapper.toResponse(repository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Role not found with id " + id);
        }
        repository.softDeleteById(id, Instant.now());
    }
}
