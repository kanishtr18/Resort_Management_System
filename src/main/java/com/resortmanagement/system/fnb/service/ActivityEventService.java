package com.resortmanagement.system.fnb.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.fnb.entity.ActivityEvent;
import com.resortmanagement.system.fnb.mapper.ActivityEventMapper;
import com.resortmanagement.system.fnb.repository.ActivityEventRepository;
import com.resortmanagement.system.hr.service.EmployeeService;

@Service
public class ActivityEventService {

    private final ActivityEventRepository repository;
    private final ActivityEventMapper mapper;
    private final EmployeeService employeeService;

    public ActivityEventService(
        ActivityEventRepository repository, 
        ActivityEventMapper mapper,
        EmployeeService employeeService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.employeeService = employeeService;
    }

    public List<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> findAllActive() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    public List<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public Optional<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> findById(UUID id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    public com.resortmanagement.system.fnb.dto.response.ActivityEventResponse create(com.resortmanagement.system.fnb.dto.request.ActivityEventRequest request) {
        ActivityEvent entity = mapper.toEntity(request, 
            employeeService.findEmployeeById(request.getInstructor())
            .orElseThrow(() -> new RuntimeException("Instructor not found: " + request.getInstructor()))
        );
        return mapper.toResponse(repository.save(entity));
    }

    public com.resortmanagement.system.fnb.dto.response.ActivityEventResponse update(UUID id, com.resortmanagement.system.fnb.dto.request.ActivityEventRequest request) {
        ActivityEvent entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity event not found: " + id));
        mapper.updateEntity(
            entity, 
            request,
            employeeService.findEmployeeById(request.getInstructor())
            .orElseThrow(() -> new RuntimeException("Instructor not found: " + request.getInstructor()))
        );
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        // Assuming soft delete if supported, but repo method was softDeleteById in view?
        // Checking view_file output from step 254: repository.softDeleteById(id);
        repository.softDeleteById(id, java.time.Instant.now());
    }
}
