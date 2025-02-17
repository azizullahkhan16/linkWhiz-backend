package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    boolean existsByPlanName(String free);

    Optional<Plan> findByPlanName(String free);
}
