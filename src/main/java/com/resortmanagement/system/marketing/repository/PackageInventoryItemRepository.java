package com.resortmanagement.system.marketing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resortmanagement.system.marketing.entity.PackageInventoryItem;

@Repository
public interface PackageInventoryItemRepository extends JpaRepository<PackageInventoryItem, UUID>{

}
