package com.aktic.linkWhiz_backend.model.response;


import com.aktic.linkWhiz_backend.model.entity.AnalyticsUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsUrlResponse {
    private Long id;
    private BigInteger clicks;
    private Instant lastAccessedAt;

    public AnalyticsUrlResponse(AnalyticsUrl analyticsUrl) {
        this.id = analyticsUrl.getId();
        this.clicks = analyticsUrl.getClicks();
        this.lastAccessedAt = analyticsUrl.getLastAccessedAt();
    }
}
