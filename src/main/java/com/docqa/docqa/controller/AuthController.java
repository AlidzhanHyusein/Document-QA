package com.docqa.docqa.controller;


import com.docqa.docqa.dto.request.LoginRequest;
import com.docqa.docqa.dto.request.RegisterRequest;
import com.docqa.docqa.dto.response.AuthResponse;
import com.docqa.docqa.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/auth")
@Tag(name = "Authentication" ,description = "User register and login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user",description = "Create a new user account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login",description = "Authenticate user and receive JWT token")

    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
