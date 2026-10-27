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

@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authService.registerUser(registerRequest), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("--> Hit AuthController login endpoint with email: " + loginRequest.getEmail());
        return new ResponseEntity<>(authService.authenticateUser(loginRequest), HttpStatus.OK);
    }
}
