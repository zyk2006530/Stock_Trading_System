package com.stocktrading.vo;

import com.stocktrading.entity.User;
import com.stocktrading.entity.enums.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private UserRole role;
    private Boolean firstLogin;
    private String certificateCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getRealName(),
            user.getPhone(),
            user.getEmail(),
            user.getRole(),
            user.getFirstLogin(),
            user.getCertificateCode(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
