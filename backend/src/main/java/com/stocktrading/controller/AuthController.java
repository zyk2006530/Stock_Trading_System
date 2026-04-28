package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.dto.AuthResponse;
import com.stocktrading.dto.FirstLoginRequest;
import com.stocktrading.dto.LoginRequest;
import com.stocktrading.dto.UserMeResponse;
import com.stocktrading.security.CustomUserDetails;
import com.stocktrading.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/first-login")
    public ApiResponse<AuthResponse> firstLogin(@Valid @RequestBody FirstLoginRequest request) {
        return ApiResponse.success(authService.firstLogin(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.logout(authHeader);
        return ApiResponse.success();
    }

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> me(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(UserMeResponse.from(userDetails.getUser()));
    }
}
