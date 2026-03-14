package com.resortmanagement.system.security.service;

import com.resortmanagement.system.common.guest.Guest;
import com.resortmanagement.system.common.guest.GuestRepository;
import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.hr.repository.EmployeeRepository;
import com.resortmanagement.system.security.dto.AuthRequest;
import com.resortmanagement.system.security.dto.AuthResponse;
import com.resortmanagement.system.security.dto.SignUpRequest;
import com.resortmanagement.system.security.entity.User;
import com.resortmanagement.system.security.enums.Role;
import com.resortmanagement.system.security.jwt.JwtService;
import com.resortmanagement.system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse signup(SignUpRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);

        if (request.getRole() == Role.EMPLOYEE || request.getRole() == Role.ADMIN) {
            var employee = Employee.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .credentialsHash(user.getPassword()) 
                    .hireDate(LocalDate.now())
                    .status(Employee.EmployeeStatus.ACTIVE)
                    .build();
            employeeRepository.save(employee);
        } else if (request.getRole() == Role.GUEST) {
            var guest = Guest.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .build();
            guestRepository.save(guest);
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }
}
