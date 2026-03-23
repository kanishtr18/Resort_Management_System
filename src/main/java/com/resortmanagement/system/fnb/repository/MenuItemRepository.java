package com.resortmanagement.system.fnb.repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.fnb.entity.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends SoftDeleteRepository<MenuItem, UUID> {

    @Query("select m from MenuItem m where m.deletedAt is null")
    List<MenuItem> findAllActive();
    List<MenuItem> findByDeletedFalse();
}
