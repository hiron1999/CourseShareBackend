package com.courseshare.AutorizationService.service;

import com.courseshare.AutorizationService.dto.AuthRequest;
import com.courseshare.AutorizationService.dto.AuthResponse;
import com.courseshare.AutorizationService.dto.UserRegistrationRequest;
import com.courseshare.AutorizationService.entity.UserCredential;
import org.springframework.http.ResponseEntity;

public interface CustomAuthInterface {

    public ResponseEntity<?> register(UserRegistrationRequest userRegistrationRequest);
    public AuthResponse generateToken(AuthRequest request);
    public boolean validateToken(String token);

    AuthResponse getbyRefreshToken(String refreshToken);
}
