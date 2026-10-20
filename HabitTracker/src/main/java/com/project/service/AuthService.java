package com.project.service;

import com.project.dto.JwtResponse;
import com.project.dto.LoginRequest;
import com.project.dto.RegisterRequest;
import com.project.dto.UserResponse;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthService {


    private UserRepository userRepository;
    private PasswordEncoder  passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    public AuthService (UserRepository userRepository, PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider =  tokenProvider;


    }
    public JwtResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {

            return new JwtResponse(null,null);
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt =  tokenProvider.generateToken(authentication);
        UserResponse userResponse = new UserResponse(registerRequest.getUsername(), registerRequest.getEmail());
        return new JwtResponse(jwt,userResponse);


    }
    public JwtResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt =  tokenProvider.generateToken(authentication);
        UserResponse userResponse = new UserResponse(loginRequest.getUsername(), loginRequest.getEmail());
        return new JwtResponse(jwt,userResponse);
    }
}
