package com.project.service;

import com.project.dto.JwtResponse;
import com.project.dto.LoginRequest;
import com.project.dto.RegisterRequest;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.security.CustomUserDetails;
import com.project.security.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider tokenProvider;
    @InjectMocks
    AuthService authService;
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
    //Todo: Need a test case for username, email and password not being correct
    @Test
    void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setEmail("email@gmail.com");
        registerRequest.setPassword("password");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("username");
        savedUser.setEmail("email@gmail.com");
        savedUser.setPassword("encodedPassword");

        Authentication authResult = new UsernamePasswordAuthenticationToken(
                registerRequest.getEmail(),
                null,
                List.of()
        );
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authResult);
        when(tokenProvider.generateToken(any())).thenReturn("mock-jwt-token");
        JwtResponse response = authService.registerUser(registerRequest);
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("username",response.getUser().getUsername());
        assertEquals("email@gmail.com",response.getUser().getEmail());


    }
    @Test
    void registerUserWithExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setEmail("email@gmail.com");
        registerRequest.setPassword("password");
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);
        JwtResponse response = authService.registerUser(registerRequest);
        assertEquals(new JwtResponse(null, null), response);

    }
    @Test
    void authenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@gmail.com");
        loginRequest.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("username");
        savedUser.setEmail("email@gmail.com");
        savedUser.setPassword("encodedPassword");
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUser()).thenReturn(savedUser);
        when(userDetails.getAuthorities()).thenReturn(List.of());

        Authentication authResult = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        when(userRepository.existsByEmail(loginRequest.getEmail())).thenReturn(true);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authResult);
        when(tokenProvider.generateToken(any())).thenReturn("mock-jwt-token");
        JwtResponse response =  authService.authenticateUser(loginRequest);
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("username",response.getUser().getUsername());
        assertEquals("email@gmail.com", response.getUser().getEmail());
        assertEquals(authResult, SecurityContextHolder.getContext().getAuthentication());
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(tokenProvider).generateToken(authResult);


    }
    @Test
    void authenticateUserWithoutExistingUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@gmail.com");
        loginRequest.setPassword("password");
        when(userRepository.existsByEmail(loginRequest.getEmail())).thenReturn(false);
        JwtResponse response =  authService.authenticateUser(loginRequest);
        assertEquals(new JwtResponse(null, null), response);
    }
}