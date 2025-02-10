package com.aktic.linkWhiz_backend.service.redirect;

import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import com.aktic.linkWhiz_backend.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedirectService {
    private final ShortUrlRepository shortUrlRepository;

    public ResponseEntity<Void> redirectToOriginalUrl(String alias) {
        try {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("").toUriString();
            Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByShortUrl(String.format("%s/%s", baseUrl, alias));

            if (shortUrlOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ShortUrl shortUrl = shortUrlOptional.get();

            // Handle expired links
            if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(Instant.now())) {
                return ResponseEntity.status(HttpStatus.GONE).build(); // 410 GONE
            }

            return ResponseEntity.status(HttpStatus.FOUND) // 302 Redirect
                    .header(HttpHeaders.LOCATION, shortUrl.getOriginalUrl())
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error occurred while getting plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
