package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Plan findByPlanName(String planName);
}
