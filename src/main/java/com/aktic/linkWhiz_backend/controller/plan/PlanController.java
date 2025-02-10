package com.aktic.linkWhiz_backend.controller.plan;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.service.plan.PlanService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanController {
    private final PlanService planService;

    @GetMapping("/plan/getPlans")
    public ResponseEntity<ApiResponse<List<Plan>>> getPlans() {
        return planService.getPlans();
    }

    @GetMapping("/plan/getPlan/{id}")
    public ResponseEntity<ApiResponse<Plan>> getPlan(@PathVariable Long id) {
        return planService.getPlan(id);
    }
}
