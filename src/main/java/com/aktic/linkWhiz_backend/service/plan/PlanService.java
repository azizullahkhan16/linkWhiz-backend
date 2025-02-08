package com.aktic.linkWhiz_backend.service.plan;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.repository.PlanRepository;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {
    private final PlanRepository planRepository;

    public ResponseEntity<ApiResponse<List<Plan>>> getPlans() {
        try {
            return ResponseEntity.ok(new ApiResponse<>(true, "Plans retrieved successfully", planRepository.findAll()));

        } catch (Exception e) {
            log.error("Unexpected error occurred while getting plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<ApiResponse<Plan>> getPlan(Long id) {
        try {
            Optional<Plan> plan = planRepository.findById(id);
            if (plan.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Plan not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Plan retrieved successfully", plan.get()));

        } catch (Exception e) {
            log.error("Unexpected error occurred while getting plan", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
