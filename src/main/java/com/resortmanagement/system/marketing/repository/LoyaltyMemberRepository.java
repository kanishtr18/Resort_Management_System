package com.resortmanagement.system.marketing.repository;

import com.resortmanagement.system.marketing.entity.LoyaltyMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoyaltyMemberRepository
                extends com.resortmanagement.system.common.repository.SoftDeleteRepository<LoyaltyMember, UUID> {

        // Find by deleted status with pagination
        Page<LoyaltyMember> findByDeletedFalse(Pageable pageable);

        // Find by guest ID with soft delete check
        Optional<LoyaltyMember> findByGuestIdAndDeletedFalse(UUID guestId);

}
