package com.stocktrading.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FirstLoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String certificateCode;
}
