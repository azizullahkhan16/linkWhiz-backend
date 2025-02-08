package com.aktic.linkWhiz_backend.exception;

import com.aktic.linkWhiz_backend.util.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("token", "Your session has expired. Please log in again.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Authentication failed", error));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleSignatureException(SignatureException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("token", "Invalid JWT signature. Please log in again.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Authentication failed", error));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMalformedJwtException(MalformedJwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("token", "Malformed JWT token. Please log in again.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Authentication failed", error));
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("token", "Unsupported JWT token. Please log in again.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Authentication failed", error));
    }
}
