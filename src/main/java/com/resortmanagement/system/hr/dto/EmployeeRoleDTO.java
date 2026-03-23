package com.resortmanagement.system.hr.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRoleDTO {
    private UUID employeeId;
    private String employeeName;
    private UUID roleId;
    private String roleName;
    // private String department;
    private LocalDate assignedDate;
    private LocalDate endDate;
}
