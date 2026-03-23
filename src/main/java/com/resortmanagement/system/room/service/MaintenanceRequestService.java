    // package com.resortmanagement.system.room.service;

    // import java.time.LocalDateTime;
    // import java.util.List;
    // import java.util.UUID;

    // import org.springframework.stereotype.Service;

    // import com.resortmanagement.system.booking.repository.BookingGuestRepository;
    // import com.resortmanagement.system.room.dto.request.MaintenanceRequestCreateRequest;
    // import com.resortmanagement.system.room.dto.response.MaintenanceRequestResponse;
    // import com.resortmanagement.system.room.entity.MaintenanceRequest;
    // import com.resortmanagement.system.room.mapper.MaintenanceRequestMapper;
    // import com.resortmanagement.system.room.repository.MaintenanceRequestRepository;

    // @Service
    // public class MaintenanceRequestService {

    //     private final MaintenanceRequestRepository repository;
    //     private final BookingGuestRepository guestRepository;

    //     public MaintenanceRequestService(
    //             MaintenanceRequestRepository repository,
    //             BookingGuestRepository guestRepository) {
    //         this.repository = repository;
    //         this.guestRepository = guestRepository;
    //     }

    //     public MaintenanceRequestResponse create(MaintenanceRequestCreateRequest request) {
    //         MaintenanceRequest entity = MaintenanceRequestMapper.toEntity(request, guestRepository);
    //         MaintenanceRequest saved = repository.save(entity);
    //         return MaintenanceRequestMapper.toResponse(saved);
    //     }

    //     public List<MaintenanceRequestResponse> getAllOpen() {
    //         return MaintenanceRequestMapper.toResponseList(repository.findByDeletedFalse());
    //     }

    //     public void close(UUID id) {
    //         MaintenanceRequest entity = repository.findById(id)
    //                 .orElseThrow(() -> new RuntimeException("MaintenanceRequest not found"));
    //         entity.setDeleted(true);
    //         entity.setResolvedAt(LocalDateTime.now());
    //         repository.save(entity);
    //     }
    // }

    package com.resortmanagement.system.room.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.common.guest.GuestRepository;
import com.resortmanagement.system.hr.repository.EmployeeRepository;
import com.resortmanagement.system.room.dto.request.MaintenanceRequestCreateRequest;
import com.resortmanagement.system.room.dto.response.MaintenanceRequestResponse;
import com.resortmanagement.system.room.entity.MaintenanceRequest;
import com.resortmanagement.system.room.enums.MaintenanceStatus;
import com.resortmanagement.system.room.mapper.MaintenanceRequestMapper;
import com.resortmanagement.system.room.repository.MaintenanceRequestRepository;

@Service
public class MaintenanceRequestService {

    private final MaintenanceRequestRepository repository;
    private final EmployeeRepository employeeRepository;  // Fix: was BookingGuestRepository
    private final GuestRepository guestRepository;

    public MaintenanceRequestService(
            MaintenanceRequestRepository repository,
            EmployeeRepository employeeRepository,
            GuestRepository guestRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.guestRepository = guestRepository;
    }

    public MaintenanceRequestResponse create(MaintenanceRequestCreateRequest request) {

        // Validate at least one reporter is provided
        if (request.getReportedByEmployeeId() == null && request.getReportedByGuestId() == null) {
            throw new RuntimeException("Either reportedByEmployeeId or reportedByGuestId is required");
        }

        MaintenanceRequest entity = new MaintenanceRequest();
        entity.setRoomOrFacilityId(request.getRoomOrFacilityId());
        entity.setDescription(request.getDescription());
        entity.setSeverity(request.getSeverity());
        entity.setStatus(request.getStatus());

        // Fix: handle both reporter types
        if (request.getReportedByEmployeeId() != null) {
            entity.setReportedByEmployee(
                employeeRepository.findByIdAndDeletedFalse(request.getReportedByEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"))
            );
        }

        if (request.getReportedByGuestId() != null) {
            entity.setReportedByGuest(
                guestRepository.findByIdAndDeletedFalse(request.getReportedByGuestId())
                    .orElseThrow(() -> new RuntimeException("Guest not found"))
            );
        }

        return MaintenanceRequestMapper.toResponse(repository.save(entity));
    }

    // Fix: filter by status instead of returning all non-deleted
    public List<MaintenanceRequestResponse> getAllOpen() {
        return MaintenanceRequestMapper.toResponseList(
            repository.findByDeletedFalseAndStatusNot(MaintenanceStatus.CLOSED)
        );
    }

    public void close(UUID id) {
        MaintenanceRequest entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaintenanceRequest not found"));
        entity.setDeleted(true);
        entity.setResolvedAt(LocalDateTime.now());
        entity.setStatus(MaintenanceStatus.CLOSED);  // Fix: also set status to CLOSED
        repository.save(entity);
    }
}