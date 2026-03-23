package com.resortmanagement.system.hr.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.hr.entity.EmployeeRole;

@Repository
public interface EmployeeRoleRepository extends SoftDeleteRepository<EmployeeRole, UUID> {
    java.util.List<EmployeeRole> findByEmployeeId(UUID employeeId);

    java.util.List<EmployeeRole> findByEmployeeIdAndEndDateIsNull(UUID employeeId);

    
}
