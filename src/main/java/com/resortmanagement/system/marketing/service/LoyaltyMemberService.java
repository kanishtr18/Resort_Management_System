package com.resortmanagement.system.marketing.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.guest.GuestService;
import com.resortmanagement.system.marketing.dto.loyaltymember.LoyaltyMemberRequest;
import com.resortmanagement.system.marketing.dto.loyaltymember.LoyaltyMemberResponse;
import com.resortmanagement.system.marketing.entity.LoyaltyMember;
import com.resortmanagement.system.marketing.mapper.LoyaltyMemberMapper;
import com.resortmanagement.system.marketing.repository.LoyaltyMemberRepository;

@Service
@Transactional
public class LoyaltyMemberService {

    private final LoyaltyMemberRepository repository;
    private final LoyaltyMemberMapper mapper;
    private final GuestService guestService;

    public LoyaltyMemberService(
        LoyaltyMemberRepository loyaltyMemberRepository,
        LoyaltyMemberMapper loyaltyMemberMapper, 
        GuestService guestService
    ) {
        this.repository = loyaltyMemberRepository;
        this.mapper = loyaltyMemberMapper;
        this.guestService = guestService;
    }

    @Transactional(readOnly = true)
    public Page<LoyaltyMemberResponse> findAll(Pageable pageable) {
        return repository.findByDeletedFalse(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<LoyaltyMemberResponse> findById(UUID id) {
        return repository.findByIdAndDeletedFalse(id).map(mapper::toResponse);
    }

    public LoyaltyMemberResponse save(LoyaltyMemberRequest dto) {
        if (dto.getGuestId() == null) {
            throw new IllegalArgumentException("Guest ID is required");
        }
        if (dto.getTier() == null || dto.getTier().isEmpty()) {
            throw new IllegalArgumentException("Tier is required");
        }

        LoyaltyMember loyaltyMember = mapper.toEntity(dto, guestService.getGuest(dto.getGuestId()));
        LoyaltyMember saved = repository.save(loyaltyMember);
        return mapper.toResponse(saved);
    }

    public LoyaltyMemberResponse update(UUID id, LoyaltyMemberRequest dto) {
        return repository.findByIdAndDeletedFalse(id)
                .map(existing -> {
                    mapper.updateEntity(existing, dto, guestService.getGuest(dto.getGuestId()));
                    return mapper.toResponse(repository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("LoyaltyMember not found with id " + id));
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("LoyaltyMember not found with id " + id);
        }
        repository.softDeleteById(id, Instant.now());
    }

    @Transactional(readOnly = true)
    public Optional<LoyaltyMemberResponse> findByGuestId(UUID guestId) {
        return repository.findByGuestIdAndDeletedFalse(guestId).map(mapper::toResponse);
    }
}
