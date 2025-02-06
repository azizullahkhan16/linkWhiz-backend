package com.aktic.linkWhiz_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "analytics_urls")
public class AnalyticsUrl {

    @Id
    private Long id;

    @Column(name = "clicks", nullable = false)
    private BigInteger clicks;

    @Column(name = "last_accessed_at", nullable = true)
    private Instant lastAccessedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "short_url_id", nullable = false)
    private ShortUrl shortUrl;

    @PrePersist
    public void prePersist() {
        this.clicks = BigInteger.ZERO;
    }

}