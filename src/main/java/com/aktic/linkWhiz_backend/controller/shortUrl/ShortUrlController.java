package com.aktic.linkWhiz_backend.controller.shortUrl;

import com.aktic.linkWhiz_backend.model.request.QRCodeRequest;
import com.aktic.linkWhiz_backend.model.request.ShortUrlRequest;
import com.aktic.linkWhiz_backend.model.request.ShortUrlResponse;
import com.aktic.linkWhiz_backend.service.shortUrl.ShortUrlService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shortUrl")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("/shorten")
    public ResponseEntity<ApiResponse<ShortUrlResponse>> shortenUrl(@Valid @RequestBody final ShortUrlRequest shortUrlRequest) {
        return shortUrlService.shortenUrl(shortUrlRequest);
    }

    @PostMapping(value = "/qrCode/{urlId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ShortUrlResponse>> generateQRCode(@Valid @ModelAttribute final QRCodeRequest qrCodeRequest,
                                                                        @PathVariable final Long urlId) {
        return shortUrlService.generateQRCode(qrCodeRequest, urlId);
    }


}
