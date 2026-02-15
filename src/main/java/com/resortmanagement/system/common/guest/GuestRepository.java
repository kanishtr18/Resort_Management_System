/*
GuestRepository.java
Purpose:
 - Extend SoftDeleteRepository<Guest, UUID> which have soft delete methods and extends jpa repository.
 - Provide methods: findByEmail, findByPhone, findByLoyaltyId
 - SoftDeleteRepository<Guest, UUID> provide methods:
        - findByDeletedFalse, 
        - softDeleteById
Notes:
 - Ensure email unique constraints at DB level via Flyway migration.
File: common/Guest/guestRepository.java
*/

package com.resortmanagement.system.common.guest;

import java.util.Optional;
import java.util.UUID;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;

public interface GuestRepository extends SoftDeleteRepository<Guest, UUID>{
    
    // Used during guest creation & validation
    Optional<Guest> findByEmail(String email);

    Optional<Guest> findByPhone(String phone);

    // Safety checks (used before create/update)
    boolean existsByEmail(String email);
}