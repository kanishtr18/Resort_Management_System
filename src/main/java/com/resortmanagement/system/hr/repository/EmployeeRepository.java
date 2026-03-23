package com.resortmanagement.system.hr.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.resortmanagement.system.common.repository.SoftDeleteRepository;
import com.resortmanagement.system.hr.entity.Employee;

@Repository
public interface EmployeeRepository extends SoftDeleteRepository<Employee, UUID> {
    java.util.Optional<Employee> findByEmail(String email);

    org.springframework.data.domain.Page<Employee> findByDeletedFalse(
            org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Employee> findByStatusAndDeletedFalse(
            Employee.EmployeeStatus status,
            org.springframework.data.domain.Pageable pageable);
}
