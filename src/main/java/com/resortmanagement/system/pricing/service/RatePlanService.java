package com.resortmanagement.system.pricing.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.pricing.dto.request.RatePlanCreateRequest;
import com.resortmanagement.system.pricing.dto.request.RatePlanUpdateRequest;
import com.resortmanagement.system.pricing.dto.response.RatePlanResponse;
import com.resortmanagement.system.pricing.dto.response.RatePlanSummaryResponse;
import com.resortmanagement.system.pricing.entity.RatePlan;
import com.resortmanagement.system.pricing.repository.RatePlanRepository;
import com.resortmanagement.system.room.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RatePlanService {

    private final RatePlanRepository repository;
    private final RoomTypeRepository roomTypeRepository;
    
    public RatePlanResponse create(RatePlanCreateRequest request) {

        RatePlan plan = new RatePlan();
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setBasePrice(request.getBasePrice());
        plan.setIsRefundable(request.getRefundable());
        plan.setMinStayNights(request.getMinStayNights());
        plan.setMaxStayNights(request.getMaxStayNights());
        plan.setRoomTypeId(roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found")));
        plan.setValidFrom(request.getValidFrom());
        plan.setValidTo(request.getValidTo());
        return toResponse(repository.save(plan));
    }

    public RatePlanResponse update(UUID id, RatePlanUpdateRequest request) {

        RatePlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RatePlan not found"));

        if (request.getDescription() != null)
            plan.setDescription(request.getDescription());

        if (request.getBasePrice() != null)
            plan.setBasePrice(request.getBasePrice());

        return toResponse(plan);
    }

    @Transactional(readOnly = true)
    public List<RatePlanSummaryResponse> list() {
        return repository.findByDeletedFalse().stream()
                .map(p -> RatePlanSummaryResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .basePrice(p.getBasePrice())
                        .build())
                .toList();
    }

    private RatePlanResponse toResponse(RatePlan plan) {
        return RatePlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .basePrice(plan.getBasePrice())
                .refundable(plan.getIsRefundable())
                .minStayNights(plan.getMinStayNights())
                .maxStayNights(plan.getMaxStayNights())
                .build();
    }
}

