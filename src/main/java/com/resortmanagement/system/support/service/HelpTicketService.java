package com.resortmanagement.system.support.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.hr.repository.EmployeeRepository;
import com.resortmanagement.system.support.dto.request.HelpTicketCreateRequest;
import com.resortmanagement.system.support.dto.request.HelpTicketUpdateRequest;
import com.resortmanagement.system.support.dto.response.HelpTicketResponse;
import com.resortmanagement.system.support.entity.HelpTicket;
import com.resortmanagement.system.support.enums.TicketStatus;
import com.resortmanagement.system.support.mapper.HelpTicketMapper;
import com.resortmanagement.system.support.repository.HelpTicketRepository;

import jakarta.transaction.Transactional;

@Service
public class HelpTicketService {

    private final HelpTicketRepository repository;
    private final HelpTicketMapper mapper;
    private final EmployeeRepository employeeRepository; // Fix: was GuestRepository

    public HelpTicketService(
            HelpTicketRepository repository,
            HelpTicketMapper mapper,
            EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
    }

    public HelpTicketResponse create(HelpTicketCreateRequest request) {
        HelpTicket entity = new HelpTicket();
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setPriority(request.getPriority());
        entity.setStatus(TicketStatus.OPEN);
        entity.setTicketNumber("TKT-" + System.currentTimeMillis());
        return mapper.toResponse(repository.save(entity));
    }

    public List<HelpTicketResponse> getAll() {
        return repository.findByDeletedFalse()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public HelpTicketResponse update(UUID id, HelpTicketUpdateRequest request) {
        HelpTicket entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (request.getPriority() != null)
            entity.setPriority(request.getPriority());

        if (request.getStatus() != null)
            entity.setStatus(request.getStatus());

        if (request.getAssignedTo() != null) {
            // Fix: was using guestRepository
            entity.setAssignedTo(
                employeeRepository.findByIdAndDeletedFalse(request.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Employee not found"))
            );
        }
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
        HelpTicket entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        entity.setDeleted(true);
        repository.save(entity);
    }

    @Transactional
    public HelpTicketResponse updateStatus(UUID id, TicketStatus status) {
        HelpTicket ticket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
        ticket.setStatus(status);
        return mapper.toResponse(repository.save(ticket));
    }
}