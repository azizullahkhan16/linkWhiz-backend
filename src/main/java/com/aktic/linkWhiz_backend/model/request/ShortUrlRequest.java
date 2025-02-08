package com.aktic.linkWhiz_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortUrlRequest {
    @NotBlank(message = "Original URL is required")
    private String originalUrl;
    private String customAlias;
    private Instant expiresAt;
}
