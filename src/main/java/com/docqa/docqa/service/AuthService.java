package com.docqa.docqa.service;

import com.docqa.docqa.dto.request.LoginRequest;
import com.docqa.docqa.dto.request.RegisterRequest;
import com.docqa.docqa.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
