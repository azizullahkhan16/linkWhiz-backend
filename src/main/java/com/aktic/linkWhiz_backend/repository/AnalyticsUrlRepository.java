package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.entity.AnalyticsUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalyticsUrlRepository extends JpaRepository<AnalyticsUrl, Long> {

    Optional<AnalyticsUrl> findByShortUrlId(Long shortUrlId);
}
