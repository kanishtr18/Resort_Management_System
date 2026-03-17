/*
guestService.java
Purpose:
 - Business logic for guest management (merge duplicates, GDPR export/erase).
Methods:
 - createGuest(CreateGuestDto)
 - updateGuest(UUID id, UpdateGuestDto)
 - mergeGuest(UUID sourceId, UUID targetId) // reassign bookings/payments
 - anonymizeGuest(UUID id) // for GDPR erase
Guidance:
 - Use transactions for merge operations.
 - Use Auditable for traceability.
File: common/Guest/guestService.java
*/

package com.resortmanagement.system.common.guest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.common.guest.dto.guestResponseDto;
import com.resortmanagement.system.marketing.entity.LoyaltyMember;
import com.resortmanagement.system.marketing.repository.LoyaltyMemberRepository;
import com.resortmanagement.system.marketing.entity.LoyaltyMember.MemberStatus;

@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final LoyaltyMemberRepository loyaltyMemberRepository;

    public GuestService(GuestRepository guestRepository, LoyaltyMemberRepository loyaltyMemberRepository) {
        this.guestRepository = guestRepository;
        this.loyaltyMemberRepository = loyaltyMemberRepository;
    }

    // CREATE
    @Transactional
    public Guest createGuest(Guest guest) {
        if (guestRepository.existsByEmail(guest.getEmail())) {
            throw new ApplicationException("Guest with email already exists");
        }

        guest = guestRepository.save(guest);

        LoyaltyMember loyaltyMember = LoyaltyMember.builder()
                .guest(guest)
                .tier("BRONZE")
                .pointsBalance(BigDecimal.ZERO)
                .enrolledAt(Instant.now())
                .status(MemberStatus.ACTIVE)
                .build();

        loyaltyMember = loyaltyMemberRepository.save(loyaltyMember);

        guest.setLoyaltyMember(loyaltyMember);

        return guestRepository.save(guest);
    }

    //READ
    @Transactional(readOnly = true)
    public List<guestResponseDto> getAllGuests() {
        return guestRepository.findAll().stream()
                .map(guest -> guestResponseDto.builder()
                        .id(guest.getId())
                        .firstName(guest.getFirstName())
                        .lastName(guest.getLastName())
                        .email(guest.getEmail())
                        .guestType(guest.getGuestType())
                        .phone(guest.getPhone())
                        .address(guest.getAddress())
                        .dob(guest.getDob())
                        .age(guest.getAge())
                        .loyaltyId(guest.getLoyaltyMember().getId())
                        .loyaltyTier(guest.getLoyaltyMember().getTier())
                        .pointsBalance(guest.getLoyaltyMember().getPointsBalance())
                        .build())
                .toList();
    }

    // READ
    @Transactional(readOnly = true)
    public Guest getGuest(UUID guestId) {
        return guestRepository.findByIdAndDeletedFalse(guestId)
                .orElseThrow(() -> new ApplicationException("Guest not found"));
    }

    // UPDATE
    @Transactional
    public Guest updateGuest(UUID guestId, Guest updatedGuest) {
        Guest existing = getGuest(guestId);

        existing.setFirstName(updatedGuest.getFirstName());
        existing.setLastName(updatedGuest.getLastName());
        existing.setPhone(updatedGuest.getPhone());
        existing.setAddress(updatedGuest.getAddress());
        existing.setDob(updatedGuest.getDob());

        return guestRepository.save(existing);
    }

    // SOFT DELETE
    @Transactional
    public void deleteGuest(UUID guestId) {
        guestRepository.softDeleteById(guestId, Instant.now());
    }

    // GDPR ANONYMIZATION (VERY IMPORTANT)
    @Transactional
    public void anonymizeGuest(UUID guestId) {
        Guest guest = getGuest(guestId);

        guest.setFirstName("ANONYMIZED");
        guest.setLastName("ANONYMIZED");
        guest.setEmail("anon-" + guestId + "@example.com");
        guest.setPhone((String) null);
        guest.setAddress((String) null);
        guest.setDob((java.time.LocalDate) null);

        guestRepository.save(guest);
    }

    /*
     * FUTURE (do not implement now, but planned):
     * mergeGuest(UUID sourceId, UUID targetId)
     * - Move reservations, invoices, orders
     * - Soft delete source guest
     */
}
