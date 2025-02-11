package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import com.aktic.linkWhiz_backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByOriginalUrlAndUser(String originalUrl, User user);

    boolean existsByCustomAlias(String customAlias);

    Optional<ShortUrl> findByShortUrl(String baseUrl);

    Optional<ShortUrl> findByIdAndUserId(Long id, Long userId);

    Page<ShortUrl> findAllByUserId(Long userId, Pageable pageable);

    Page<ShortUrl> findByUserIdAndCreatedAtAndExpiresAt(Long userId, Instant createdAt, Instant expiresAt, Pageable pageable);

    Page<ShortUrl> findByUserIdAndCreatedAt(Long userId, Instant createdAt, Pageable pageable);

    Page<ShortUrl> findByUserIdAndExpiresAt(Long userId, Instant expiresAt, Pageable pageable);

    Page<ShortUrl> findByUserId(Long userId, Pageable pageable);

    Page<ShortUrl> findByUserIdAndCreatedAtBetweenAndExpiresAtBetween(Long userId, Instant createdAtStart, Instant createdAtEnd, Instant expiresAtStart, Instant expiresAtEnd, Pageable pageable);

    Page<ShortUrl> findByUserIdAndCreatedAtBetween(Long userId, Instant createdAtStart, Instant createdAtEnd, Pageable pageable);

    Page<ShortUrl> findByUserIdAndExpiresAtBetween(Long userId, Instant expiresAtStart, Instant expiresAtEnd, Pageable pageable);
}
