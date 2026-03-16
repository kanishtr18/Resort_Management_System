package com.resortmanagement.system.security.controller;

import com.resortmanagement.system.security.dto.AuthRequest;
import com.resortmanagement.system.security.dto.AuthResponse;
import com.resortmanagement.system.security.dto.SignUpRequest;
import com.resortmanagement.system.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    // FIX: @Valid added — without it, all @NotBlank / @Email annotations on SignUpRequest are silently ignored
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}