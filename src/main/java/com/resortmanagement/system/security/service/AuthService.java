package com.resortmanagement.system.security.service;

import java.math.BigDecimal;
import java.time.Instant;
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
import com.resortmanagement.system.common.guest.GuestRepository;
import com.resortmanagement.system.hr.dto.employee.EmployeeRequest;
import com.resortmanagement.system.hr.entity.Employee.EmployeeStatus;
import com.resortmanagement.system.hr.service.EmployeeService;
import com.resortmanagement.system.marketing.dto.loyaltymember.LoyaltyMemberRequest;
import com.resortmanagement.system.marketing.entity.LoyaltyMember;
import com.resortmanagement.system.marketing.entity.LoyaltyMember.MemberStatus;
import com.resortmanagement.system.marketing.mapper.LoyaltyMemberMapper;
import com.resortmanagement.system.marketing.repository.LoyaltyMemberRepository;
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

    private final LoyaltyMemberRepository loyaltyMemberRepository;

    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    // FIX: Was "EmployeeService" (capital E) — Java variable names must start lowercase
    private final EmployeeService employeeService;

    @Transactional
    public AuthResponse signup(SignUpRequest request) {
        // FIX: No duplicate check existed — duplicate email caused a DataIntegrityViolationException (500)
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        // FIX: No null role guard — null role caused NullPointerException in the if-check below
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
                    .hireDate(LocalDate.now())
                    .phone(request.getPhone())
                    .status(EmployeeStatus.ACTIVE)
                    .build();
            // FIX: Was calling "EmployeeService.save()" — now correctly calls "employeeService.save()"
            employeeService.save(employee);

        } else if (request.getRole() == Role.GUEST) {

            if (guestRepository.existsByEmail(request.getEmail())) {
                throw new ApplicationException("Guest with email already exists");
            }

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

            LoyaltyMemberRequest loyaltyMemberDto =
                    LoyaltyMemberRequest.builder()
                            .tier("BRONZE")
                            .pointsBalance(BigDecimal.ZERO)
                            .enrolledAt(Instant.now())
                            .status(MemberStatus.ACTIVE)
                            .build();

            LoyaltyMember loyaltyMember =
                new LoyaltyMemberMapper().toEntity(loyaltyMemberDto, guest);

            loyaltyMemberRepository.save(loyaltyMember);

            guest.setLoyaltyMember(loyaltyMember);

            guestRepository.save(guest);
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