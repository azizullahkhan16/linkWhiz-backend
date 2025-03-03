package com.aktic.linkWhiz_backend.controller.oauth2;

import com.aktic.linkWhiz_backend.model.response.AuthenticationResponse;
import com.aktic.linkWhiz_backend.service.oauth2.OAuthService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/oauth2")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService service;

    @PostMapping(value = "/callback")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> oauth2Login(
            @RequestParam String provider, @RequestParam String code) {
        return service.processOAuthLogin(provider, code);
    }


}
