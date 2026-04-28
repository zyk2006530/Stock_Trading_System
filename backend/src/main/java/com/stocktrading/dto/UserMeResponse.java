package com.stocktrading.dto;

import com.stocktrading.entity.User;
import com.stocktrading.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMeResponse {
    private Long id;
    private String username;
    private String realName;
    private UserRole role;
    private Boolean firstLogin;

    public static UserMeResponse from(User user) {
        return new UserMeResponse(
            user.getId(),
            user.getUsername(),
            user.getRealName(),
            user.getRole(),
            user.getFirstLogin()
        );
    }
}
