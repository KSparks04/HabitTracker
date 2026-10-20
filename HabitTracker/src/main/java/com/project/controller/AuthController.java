package com.project.controller;

import com.project.dto.JwtResponse;
import com.project.dto.LoginRequest;
import com.project.dto.RegisterRequest;
import com.project.model.User;
import com.project.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController


public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/api/auth/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authService.registerUser(registerRequest), HttpStatus.OK);
    }
    @PostMapping("/api/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authenticateUser(loginRequest), HttpStatus.OK);
    }
}
