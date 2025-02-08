package com.aktic.linkWhiz_backend.service.shortUrl;

import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.request.ShortUrlRequest;
import com.aktic.linkWhiz_backend.model.request.ShortUrlResponse;
import com.aktic.linkWhiz_backend.repository.ShortUrlRepository;
import com.aktic.linkWhiz_backend.service.hashing.HashingService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final HashingService hashingService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public ResponseEntity<ApiResponse<ShortUrlResponse>> shortenUrl(ShortUrlRequest shortUrlRequest) {
        try {
            // Get authenticated user directly
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            // Check if user already shortened this URL
            Optional<ShortUrl> existingShortUrl = shortUrlRepository.findByOriginalUrlAndUser(shortUrlRequest.getOriginalUrl(), user);
            if (existingShortUrl.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Shortened URL retrieved successfully", new ShortUrlResponse(existingShortUrl.get())));
            }

            // Validate expiresAt: Must be in the future and converted to UTC
            Instant nowUtc = Instant.now();
            Instant expiresAtUtc = shortUrlRequest.getExpiresAt() != null
                    ? shortUrlRequest.getExpiresAt().atZone(ZoneOffset.UTC).toInstant()
                    : null;

            if (expiresAtUtc != null && expiresAtUtc.isBefore(nowUtc)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Expiration time must be in the future", null));
            }

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("").toUriString();
            String fullShortUrl;
            // Generate unique ID and hash only when needed
            Long uniqueId = snowflakeIdGenerator.nextId();

            if (shortUrlRequest.getCustomAlias() != null) {
                // Check if custom alias already exists
                if (shortUrlRepository.existsByCustomAlias(shortUrlRequest.getCustomAlias())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>(false, "Custom alias already exists", null));
                }
                fullShortUrl = baseUrl + "/" + shortUrlRequest.getCustomAlias();
            } else {

                fullShortUrl = baseUrl + "/" + hashingService.hash(uniqueId);
            }

            // Create and save new short URL
            ShortUrl newShortUrl = ShortUrl.builder()
                    .id(uniqueId)
                    .originalUrl(shortUrlRequest.getOriginalUrl())
                    .shortUrl(fullShortUrl)
                    .customAlias(shortUrlRequest.getCustomAlias())
                    .user(user)
                    .expiresAt(expiresAtUtc)
                    .build();

            newShortUrl = shortUrlRepository.save(newShortUrl);

            return ResponseEntity.ok(new ApiResponse<>(true, "URL shortened successfully", new ShortUrlResponse(newShortUrl)));

        } catch (Exception e) {
            log.error("Unexpected error occurred while shortening URL", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
