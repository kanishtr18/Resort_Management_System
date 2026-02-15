package com.resortmanagement.system.hr.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.hr.dto.employee.EmployeeRequest;
import com.resortmanagement.system.hr.dto.employee.EmployeeResponse;
import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.hr.mapper.EmployeeMapper;
import com.resortmanagement.system.hr.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.repository = employeeRepository;
        this.mapper = employeeMapper;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> findAll(Pageable pageable) {
        return repository.findByDeletedFalse(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeResponse> findById(UUID id) {
        return repository.findByIdAndDeletedFalse(id).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeById(UUID id) {
        return repository.findByIdAndDeletedFalse(id);
    }

    public EmployeeResponse save(EmployeeRequest dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        Employee employee = mapper.toEntity(dto);
        Employee saved = repository.save(employee);
        return mapper.toResponse(saved);
    }

    public EmployeeResponse update(UUID id, EmployeeRequest dto) {
        return repository.findByIdAndDeletedFalse(id)
                .map(existing -> {
                    mapper.updateEntity(existing, dto);
                    return mapper.toResponse(repository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Employee not found with id " + id);
        }
        repository.softDeleteById(id, Instant.now());
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> findAvailableEmployees(Pageable pageable) {
        return repository.findByStatusAndDeletedFalse(Employee.EmployeeStatus.ACTIVE, pageable)
                .map(mapper::toResponse);
    }
}
