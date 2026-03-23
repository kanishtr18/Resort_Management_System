package com.resortmanagement.system.security.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.security.enums.Role;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    // FIX: Added @NotNull so a missing role field gives 400 instead of NPE
    @NotNull(message = "Role is required")
    private Role role;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob; 

    private Integer age;

    // Only for Employees
    @Nullable
    private String department;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @Nullable
    private Employee.EmployeeStatus status;
}