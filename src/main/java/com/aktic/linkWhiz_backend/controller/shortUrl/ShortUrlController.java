package com.aktic.linkWhiz_backend.controller.shortUrl;

import com.aktic.linkWhiz_backend.model.request.ShortUrlRequest;
import com.aktic.linkWhiz_backend.model.request.ShortUrlResponse;
import com.aktic.linkWhiz_backend.service.shortUrl.ShortUrlService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@RequestMapping("/api/shortUrl")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("/shorten")
    public ResponseEntity<ApiResponse<ShortUrlResponse>> shortenUrl(@Valid @RequestBody final ShortUrlRequest shortUrlRequest) {
        return shortUrlService.shortenUrl(shortUrlRequest);
    }

    @PostMapping(value = "/qrCode/{urlId}")
    public ResponseEntity<ApiResponse<ShortUrlResponse>> generateQRCode(@PathVariable final Long urlId,
                                                                        @RequestParam(required = false) Integer height,
                                                                        @RequestParam(required = false) Integer width,
                                                                        @RequestParam(required = false) String offColor,
                                                                        @RequestParam(required = false) String onColor,
                                                                        @RequestParam(required = false) MultipartFile image) {
        return shortUrlService.generateQRCode(urlId, image, width, height, offColor, onColor);
    }

    @GetMapping("/qrCode/{urlId}")
    public ResponseEntity<Resource> getQRCode(@PathVariable final Long urlId) {
        return shortUrlService.getQRCode(urlId);
    }

    @GetMapping("/getShortUrlById/{shortUrlId}")
    public ResponseEntity<ApiResponse<ShortUrlResponse>> getShortUrl(@PathVariable final Long shortUrlId) {
        return shortUrlService.getShortUrl(shortUrlId);
    }

    @GetMapping("/getShortUrls")
    public ResponseEntity<ApiResponse<Page<ShortUrlResponse>>> getShortUrls(@RequestParam(required = false) Integer pageNumber,
                                                                            @RequestParam(required = false) Integer limit,
                                                                            @RequestParam(required = false) String createdAt,
                                                                            @RequestParam(required = false) String expiresAt
    ) {
        return shortUrlService.getShortUrls(pageNumber, limit, createdAt, expiresAt);
    }

    @PatchMapping("/updateShortUrl/{shortUrlId}")
    public ResponseEntity<ApiResponse<ShortUrlResponse>> updateShortUrl(@PathVariable final Long shortUrlId,
                                                                        @RequestParam(required = false) Instant expiresAt,
                                                                        @RequestParam(required = false) String originalUrl,
                                                                        @RequestParam(required = false) Boolean hasExpiry) {
        return shortUrlService.updateShortUrl(shortUrlId, expiresAt, originalUrl, hasExpiry);
    }

    @DeleteMapping("/deleteShortUrl/{shortUrlId}")
    public ResponseEntity<ApiResponse<String>> deleteShortUrl(@PathVariable final Long shortUrlId) {
        return shortUrlService.deleteShortUrl(shortUrlId);
    }


}
