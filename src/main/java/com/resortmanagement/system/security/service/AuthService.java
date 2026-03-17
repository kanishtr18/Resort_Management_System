package com.resortmanagement.system.security.service;

import java.time.LocalDate;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.enums.GuestType;
import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.common.guest.Guest;
import com.resortmanagement.system.common.guest.GuestService;
import com.resortmanagement.system.hr.dto.EmployeeRoleDTO;
import com.resortmanagement.system.hr.dto.employee.EmployeeRequest;
import com.resortmanagement.system.hr.repository.RoleRepository;
import com.resortmanagement.system.hr.service.EmployeeRoleService;
import com.resortmanagement.system.hr.service.EmployeeService;
import com.resortmanagement.system.security.dto.AuthRequest;
import com.resortmanagement.system.security.dto.AuthResponse;
import com.resortmanagement.system.security.dto.SignUpRequest;
import com.resortmanagement.system.security.entity.User;
import com.resortmanagement.system.security.enums.Role;
import com.resortmanagement.system.security.jwt.JwtService;
import com.resortmanagement.system.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final EmployeeRoleService employeeRoleService;
    private final RoleRepository roleRepository;
    private final GuestService guestService;

    @Transactional
    public AuthResponse signup(SignUpRequest request) {
        if (request.getRole() == null) {
            throw new IllegalArgumentException("Role must be specified: GUEST, EMPLOYEE, or ADMIN.");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepository.save(user);

        if (request.getRole() == Role.EMPLOYEE || request.getRole() == Role.ADMIN) {
            var employee = EmployeeRequest.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .hireDate(request.getHireDate())
                    .phone(request.getPhone())
                    .status(request.getStatus())
                    .build();
            // FIX: Was calling "EmployeeService.save()" — now correctly calls "employeeService.save()"
            var savedEmployee = employeeService.save(employee);

            if (request.getDepartment() != null) {
                var role = roleRepository.findByName(
                    request.getDepartment()
                ).orElseThrow(
                    () -> new ApplicationException("Role not found")
                );
                EmployeeRoleDTO employeeRoleDto = new EmployeeRoleDTO(
                    savedEmployee.getId(),
                    savedEmployee.getFirstName() + " " + savedEmployee.getLastName(),
                    role.getId(),
                    request.getDepartment(),
                    LocalDate.now(),
                    null
                );
                employeeRoleService.save(employeeRoleDto);
            }

        } else if (request.getRole() == Role.GUEST) {

            Guest guest = Guest.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .age(request.getAge())
                    .guestType(GuestType.fromAge(request.getAge()))
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .dob(request.getDob())
                    .build();
            
            guestService.createGuest(guest);
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        // FIX: BadCredentialsException was unhandled — now caught and re-thrown with a clear message
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        // FIX: orElseThrow() with no args throws NoSuchElementException (500) — now descriptive
        var user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found after successful authentication."));

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }
}