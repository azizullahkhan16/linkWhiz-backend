package com.aktic.linkWhiz_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "short_urls")
@Builder
public class ShortURL {

    @Id
    private Long id;

    @Column(name = "original_url", nullable = false)
    private String originalURL;

    @Column(name = "short_url", nullable = false)
    private String shortURL;

    @Column(name = "custom_alias", unique = true)
    private String customAlias;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "qr_code")
    private String qrCode;

    @OneToOne(mappedBy = "shortURL", cascade = CascadeType.ALL, orphanRemoval = true)
    private AnalyticsURL analyticsURL;

}
