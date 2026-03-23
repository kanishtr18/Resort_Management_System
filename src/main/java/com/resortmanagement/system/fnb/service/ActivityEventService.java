package com.resortmanagement.system.fnb.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.enums.ActivityEventStatus;
import com.resortmanagement.system.fnb.dto.request.ActivityEventRequest;
import com.resortmanagement.system.fnb.dto.response.ActivityEventResponse;
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
    @Transactional(readOnly = true)
    public List<ActivityEventResponse> findAllActive() {
    return repository.findAll().stream()
            .filter(e -> !e.isDeleted() && e.getStatus() == ActivityEventStatus.SCHEDULED)
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ActivityEventResponse> findAll() {
    return repository.findByDeletedFalse().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
}
    @Transactional(readOnly = true)
    public Optional<com.resortmanagement.system.fnb.dto.response.ActivityEventResponse> findById(UUID id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    public ActivityEventResponse create(ActivityEventRequest request) {
    // ✅ Validate end time is after start time
    if (request.getEndTime().isBefore(request.getStartTime())) {
        throw new IllegalArgumentException("End time must be after start time.");
    }
    // ✅ Validate start time is in the future only for new events
    if (request.getStartTime().isBefore(java.time.Instant.now())) {
        throw new IllegalArgumentException("Start time must be in the future.");
    }

    ActivityEvent entity = mapper.toEntity(request,
        employeeService.findEmployeeById(request.getInstructor())
            .orElseThrow(() -> new RuntimeException("Instructor not found: " + request.getInstructor()))
    );
    return mapper.toResponse(repository.save(entity));
}

    public ActivityEventResponse update(UUID id, ActivityEventRequest request) {
    // ✅ Only check end > start for updates
    if (request.getEndTime().isBefore(request.getStartTime())) {
        throw new IllegalArgumentException("End time must be after start time.");
    }

    ActivityEvent entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Activity event not found: " + id));
    mapper.updateEntity(entity, request,
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
