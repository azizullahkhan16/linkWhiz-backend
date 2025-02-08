package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import com.aktic.linkWhiz_backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByOriginalUrlAndUser(String originalUrl, User user);

    boolean existsByCustomAlias(String customAlias);
}
