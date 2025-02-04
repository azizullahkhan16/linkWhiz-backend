package com.aktic.linkWhiz_backend.service.auth;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.request.RegisterRequest;
import com.aktic.linkWhiz_backend.repository.PlanRepository;
import com.aktic.linkWhiz_backend.repository.UserRepository;
import com.aktic.linkWhiz_backend.service.jwt.JwtService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SnowflakeIdGenerator idGenerator;

    public ResponseEntity<ApiResponse<String>> register(RegisterRequest request) {
        Optional<Plan> plan = planRepository.findById(request.getPlanId());
        if (plan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Plan not found", null));
        }
        User user = User.builder()
                .id(idGenerator.nextId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .plan(plan.get())
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", null));
    }
}
