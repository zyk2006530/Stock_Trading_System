package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.dto.StaffCreateUserRequest;
import com.stocktrading.service.StaffUserService;
import com.stocktrading.vo.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/users")
@RequiredArgsConstructor
public class StaffUserController {
    private final StaffUserService staffUserService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody StaffCreateUserRequest request) {
        return ApiResponse.success(staffUserService.createUser(request));
    }
}
