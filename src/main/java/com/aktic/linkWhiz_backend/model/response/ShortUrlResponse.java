package com.aktic.linkWhiz_backend.model.response;

import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortUrlResponse {
    private Long id;
    private String shortUrl;
    private String originalUrl;
    private String customAlias;
    private String expiresAt;
    private String createdAt;
    private String updatedAt;
    private String qrCode;

    public ShortUrlResponse(ShortUrl shortUrl) {
        this.id = shortUrl.getId();
        this.shortUrl = shortUrl.getShortUrl();
        this.originalUrl = shortUrl.getOriginalUrl();
        this.customAlias = shortUrl.getCustomAlias();
        this.expiresAt = shortUrl.getExpiresAt() != null ? shortUrl.getExpiresAt().toString() : null;
        this.createdAt = shortUrl.getCreatedAt().toString();
        this.updatedAt = shortUrl.getUpdatedAt().toString();
        this.qrCode = shortUrl.getQrCode();
    }

}
