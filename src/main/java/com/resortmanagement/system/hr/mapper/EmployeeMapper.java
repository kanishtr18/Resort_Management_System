package com.resortmanagement.system.hr.mapper;

import org.springframework.stereotype.Component;
import com.resortmanagement.system.hr.dto.employee.EmployeeRequest;
import com.resortmanagement.system.hr.dto.employee.EmployeeResponse;
import com.resortmanagement.system.hr.entity.Employee;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    private final RoleMapper roleMapper;

    public EmployeeMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * Convert Employee entity to EmployeeResponse DTO
     */
    public EmployeeResponse toResponse(Employee entity) {
        if (entity == null) {
            return null;
        }

        return EmployeeResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .hireDate(entity.getHireDate())
                .status(entity.getStatus())
                .roles(entity.getRoles() != null
                        ? entity.getRoles().stream()
                                .map(employeeRole -> roleMapper.toResponse(employeeRole.getRole()))
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert EmployeeRequest DTO to Employee entity
     */
    public Employee toEntity(EmployeeRequest request) {
        if (request == null) {
            return null;
        }

        return Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .hireDate(request.getHireDate())
                .status(request.getStatus())
                .credentialsHash("TEMP_HASH") // Placeholder, should be set properly in service
                .build();
    }

    /**
     * Update existing Employee entity from EmployeeRequest DTO
     */
    public void updateEntity(Employee entity, EmployeeRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setHireDate(request.getHireDate());
        entity.setStatus(request.getStatus());
    }
}
