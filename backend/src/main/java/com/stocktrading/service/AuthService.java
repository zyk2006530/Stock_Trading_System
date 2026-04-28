package com.stocktrading.service;

import com.stocktrading.dto.AuthResponse;
import com.stocktrading.dto.FirstLoginRequest;
import com.stocktrading.dto.LoginRequest;
import com.stocktrading.dto.UserMeResponse;
import com.stocktrading.entity.User;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BusinessException(401, "invalid credentials"));
        if (Boolean.TRUE.equals(user.getFirstLogin())) {
            throw new BusinessException(403, "first login requires certificate");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "invalid credentials");
        }
        String token = tokenService.issueToken(user);
        return new AuthResponse(token, UserMeResponse.from(user));
    }

    public AuthResponse firstLogin(FirstLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BusinessException(401, "invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "invalid credentials");
        }
        if (!Boolean.TRUE.equals(user.getFirstLogin())) {
            throw new BusinessException(400, "first login already completed");
        }
        if (user.getCertificateCode() == null || !user.getCertificateCode().equals(request.getCertificateCode())) {
            throw new BusinessException(400, "invalid certificate");
        }
        user.setFirstLogin(false);
        userRepository.save(user);
        String token = tokenService.issueToken(user);
        return new AuthResponse(token, UserMeResponse.from(user));
    }

    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenService.revoke(token);
        }
    }
}
