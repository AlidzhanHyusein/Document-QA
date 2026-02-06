package com.docqa.docqa.service.impl;

import com.docqa.docqa.dto.request.LoginRequest;
import com.docqa.docqa.dto.request.RegisterRequest;
import com.docqa.docqa.dto.response.AuthResponse;
import com.docqa.docqa.entity.User;
import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.repository.UserRepository;
import com.docqa.docqa.security.JwtUtil;
import com.docqa.docqa.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AuthServiceImpl(UserRepository repository,PasswordEncoder passwordEncoder , JwtUtil jwtUtil) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public AuthResponse register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new FileProcessingException("Username already exist");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new FileProcessingException("Email already exist");

        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role("USER")
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername());


        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(request.getEmail())
                .role(user.getRole())
                .message("User registered successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new FileProcessingException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new FileProcessingException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }
}
