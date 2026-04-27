package com.smarttraffic.exception;

import com.smarttraffic.dto.ApiResponse;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntime(RuntimeException e) {
        return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccess(AccessDeniedException e) {
        return ResponseEntity.status(403).body(new ApiResponse<>(403, "Access denied", null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCreds(BadCredentialsException e) {
        return ResponseEntity.status(401).body(new ApiResponse<>(401, "Invalid credentials", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new ApiResponse<>(500, "Server error", null));
    }
}