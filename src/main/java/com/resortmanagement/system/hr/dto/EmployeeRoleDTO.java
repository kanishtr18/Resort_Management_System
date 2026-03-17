package com.resortmanagement.system.hr.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRoleDTO {
    private UUID employeeId;
    private String employeeName;
    private UUID roleId;
    private String roleName;
    private LocalDate assignedDate;
    private LocalDate endDate;
}
