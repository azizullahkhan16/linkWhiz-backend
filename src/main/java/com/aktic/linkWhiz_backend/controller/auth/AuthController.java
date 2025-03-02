package com.aktic.linkWhiz_backend.controller.auth;

import com.aktic.linkWhiz_backend.model.request.AuthenticationRequest;
import com.aktic.linkWhiz_backend.model.request.RegisterRequest;
import com.aktic.linkWhiz_backend.model.response.AuthenticationResponse;
import com.aktic.linkWhiz_backend.service.auth.AuthService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.login(request);
    }

}
