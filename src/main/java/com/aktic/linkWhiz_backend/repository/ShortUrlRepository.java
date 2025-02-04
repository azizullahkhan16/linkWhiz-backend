package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    ShortUrl findByShortUrl(String shortUrl);
}
