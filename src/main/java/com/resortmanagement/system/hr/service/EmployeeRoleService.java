package com.resortmanagement.system.hr.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resortmanagement.system.hr.dto.EmployeeRoleDTO;
import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.hr.entity.EmployeeRole;
import com.resortmanagement.system.hr.entity.Role;
import com.resortmanagement.system.hr.repository.EmployeeRepository;
import com.resortmanagement.system.hr.repository.EmployeeRoleRepository;
import com.resortmanagement.system.hr.repository.RoleRepository;

@Service
@Transactional
public class EmployeeRoleService {

    private final EmployeeRoleRepository repository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    public EmployeeRoleService(
            EmployeeRoleRepository repository,
            EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeRoleDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeRoleDTO> findById(UUID id) {
        return repository.findById(id).map(this::toDTO);
    }

    public EmployeeRoleDTO save(EmployeeRoleDTO dto) {
        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (dto.getRoleId() == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        EmployeeRole entity = new EmployeeRole();

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        entity.setEmployee(employee);

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        entity.setRole(role);

        entity.setAssignedDate(dto.getAssignedDate());
        entity.setEndDate(dto.getEndDate());

        return toDTO(repository.save(entity));
    }

    public EmployeeRoleDTO update(UUID id, EmployeeRoleDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    // Update relationships if changed
                    if (dto.getEmployeeId() != null && !existing.getEmployee().getId().equals(dto.getEmployeeId())) {
                        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
                        existing.setEmployee(employee);
                    }
                    if (dto.getRoleId() != null && !existing.getRole().getId().equals(dto.getRoleId())) {
                        Role role = roleRepository.findById(dto.getRoleId())
                                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
                        existing.setRole(role);
                    }

                    existing.setAssignedDate(dto.getAssignedDate());
                    existing.setEndDate(dto.getEndDate());
                    return toDTO(repository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("EmployeeRole not found with id " + id));
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EmployeeRole not found with id " + id);
        }
        repository.deleteById(id);
    }

    // Simple DTO mapping
    private EmployeeRoleDTO toDTO(EmployeeRole entity) {
        EmployeeRoleDTO dto = new EmployeeRoleDTO();
        dto.setEmployeeId(entity.getEmployee() != null ? entity.getEmployee().getId() : null);
        dto.setRoleId(entity.getRole() != null ? entity.getRole().getId() : null);
        dto.setAssignedDate(entity.getAssignedDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }
    
    public List<String> getPermissionsForEmployee(UUID employeeId) {
        return repository.findByEmployeeIdAndEndDateIsNull(employeeId)
            .stream()
            .flatMap(er -> {
                String json = er.getRole().getPermissionsJson();
                if (json == null || json.isBlank()) return java.util.stream.Stream.empty();
                try {
                    // Fix: use injected objectMapper, not new ObjectMapper()
                    List<String> perms = objectMapper.readValue(json,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                    return perms.stream();
                } catch (Exception e) {
                    return java.util.stream.Stream.empty();
                }
            })
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }
}
