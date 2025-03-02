package com.aktic.linkWhiz_backend.service.auth;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.model.entity.Role;
import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.enums.AuthProvider;
import com.aktic.linkWhiz_backend.model.request.AuthenticationRequest;
import com.aktic.linkWhiz_backend.model.request.RegisterRequest;
import com.aktic.linkWhiz_backend.model.response.AuthenticationResponse;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.repository.PlanRepository;
import com.aktic.linkWhiz_backend.repository.RoleRepository;
import com.aktic.linkWhiz_backend.repository.UserRepository;
import com.aktic.linkWhiz_backend.security.UserPrincipal;
import com.aktic.linkWhiz_backend.security.oauth2.OAuth2UserInfo;
import com.aktic.linkWhiz_backend.service.jwt.JwtService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SnowflakeIdGenerator idGenerator;
    private final RoleRepository roleRepository;

    public ResponseEntity<ApiResponse<String>> register(RegisterRequest request) {
        try {
            Optional<Plan> plan = planRepository.findById(request.getPlanId());
            if (plan.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Plan not found", null));
            }
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Email is already in use", null));
            }
            Role userRole = roleRepository.findByRoleName("USER");
            if (userRole == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Role not found", null));
            }
            User user = User.builder()
                    .id(idGenerator.nextId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .plan(plan.get())
                    .role(userRole)
                    .build();

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", null));
        } catch (Exception e) {
            log.error("Error occurred while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(AuthenticationRequest request) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "User not found", null));
            }

            User user = optionalUser.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid password", null));
            }
            String token = jwtService.generateToken(user.getEmail());
            UserInfo userInfo = new UserInfo(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully",
                    new AuthenticationResponse(userInfo, token)));

        } catch (Exception e) {
            log.error("Unexpected error occurred while logging in user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    @Transactional
    public User registerOauth2User(String provider, OAuth2UserInfo oAuth2UserInfo) {
        try {
            Optional<Plan> plan = planRepository.findByPlanName("Free");
            if (plan.isEmpty()) {
                throw new Exception("Plan not found");
            }
            Role userRole = roleRepository.findByRoleName("USER");
            if (userRole == null) {
                throw new Exception("Role not found");
            }
            User user = User.builder()
                    .id(idGenerator.nextId())
                    .firstName(oAuth2UserInfo.getFirstName())
                    .lastName(oAuth2UserInfo.getLastName())
                    .email(oAuth2UserInfo.getEmail())
                    .plan(plan.get())
                    .role(userRole)
                    .provider(AuthProvider.valueOf(provider.toUpperCase()))
                    .build();

            log.info("User registered successfully: {}", user);
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error occurred while registering user", e);
            return null;
        }
    }

    @Transactional
    public User updateOauth2User(User user, String provider, OAuth2UserInfo oAuth2UserInfo) {
        try {
            user.setFirstName(oAuth2UserInfo.getFirstName());
            user.setLastName(oAuth2UserInfo.getLastName());
            user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
            log.info("User registered successfully: {}", user);
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error occurred while updating user", e);
            return null;
        }
    }

    public User getCurrentUser() {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userPrincipal.getUser();
        } catch (Exception e) {
            log.error("Error occurred while getting current user", e);
            return null;
        }
    }

}
