package com.health_care.gateway.exceptions;

import com.health_care.gateway.domain.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingAuthHeader(MissingAuthorizationHeaderException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.toString(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingAuthHeader(ExpiredJwtException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.toString(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
