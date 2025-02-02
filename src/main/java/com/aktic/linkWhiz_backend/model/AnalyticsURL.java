package com.aktic.linkWhiz_backend.model;

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
public class AnalyticsURL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id;

    @Column(name = "clicks", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private BigInteger clicks;

    @Column(name = "last_accessed_at", nullable = true)
    private Instant lastAccessedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "short_url_id", nullable = false)
    private ShortURL shortURL;

}