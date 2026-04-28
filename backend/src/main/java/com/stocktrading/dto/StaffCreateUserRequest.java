package com.stocktrading.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StaffCreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String realName;

    private String phone;

    @Email
    private String email;

    private String certificateCode;
}
