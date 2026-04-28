package com.stocktrading.exception;

import com.stocktrading.common.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException ex) {
        Optional<FieldError> error = ex.getBindingResult().getFieldErrors().stream().findFirst();
        String message = error.map(e -> e.getField() + " " + e.getDefaultMessage())
            .orElse("validation error");
        return ApiResponse.error(400, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolation(ConstraintViolationException ex) {
        return ApiResponse.error(400, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Void> handleAuthException(AuthenticationException ex) {
        return ApiResponse.error(401, "unauthorized");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> handleAccessDenied(AccessDeniedException ex) {
        return ApiResponse.error(403, "forbidden");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneric(Exception ex) {
        return ApiResponse.error(500, "internal server error");
    }
}
