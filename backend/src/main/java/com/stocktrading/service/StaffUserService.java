package com.stocktrading.service;

import com.stocktrading.dto.StaffCreateUserRequest;
import com.stocktrading.entity.User;
import com.stocktrading.entity.enums.UserRole;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.vo.UserResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(StaffCreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "username already exists");
        }
        String certificateCode = normalize(request.getCertificateCode());
        if (certificateCode == null) {
            certificateCode = generateCertificate();
        }
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .realName(request.getRealName())
            .phone(request.getPhone())
            .email(request.getEmail())
            .role(UserRole.USER)
            .firstLogin(true)
            .certificateCode(certificateCode)
            .build();
        userRepository.save(user);
        return UserResponse.from(user);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String generateCertificate() {
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        return "CERT-" + token;
    }
}
