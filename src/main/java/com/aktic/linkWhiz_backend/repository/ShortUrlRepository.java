package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import com.aktic.linkWhiz_backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByOriginalUrlAndUser(String originalUrl, User user);

    boolean existsByCustomAlias(String customAlias);

    Optional<ShortUrl> findByShortUrl(String baseUrl);

    Optional<ShortUrl> findByIdAndUserId(Long id, Long userId);

    Page<ShortUrl> findByUserId(Long userId, Pageable pageable);

    Page<ShortUrl> findByUserIdAndCreatedAtBetweenAndExpiresAtBetween(Long userId, Instant createdAtStart, Instant createdAtEnd, Instant expiresAtStart, Instant expiresAtEnd, Pageable pageable);

    Page<ShortUrl> findByUserIdAndCreatedAtBetween(Long userId, Instant createdAtStart, Instant createdAtEnd, Pageable pageable);

    Page<ShortUrl> findByUserIdAndExpiresAtBetween(Long userId, Instant expiresAtStart, Instant expiresAtEnd, Pageable pageable);

    boolean existsByIdAndUserId(Long shortUrlId, Long id);

    @Modifying
    @Query("DELETE FROM ShortUrl s WHERE s.id = :shortUrlId")
    void deleteShortUrl(Long shortUrlId);


}
