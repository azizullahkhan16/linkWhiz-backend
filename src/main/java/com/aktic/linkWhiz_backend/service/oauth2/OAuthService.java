package com.aktic.linkWhiz_backend.service.oauth2;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.model.entity.Role;
import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.enums.AuthProvider;
import com.aktic.linkWhiz_backend.model.response.AuthenticationResponse;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.repository.PlanRepository;
import com.aktic.linkWhiz_backend.repository.RoleRepository;
import com.aktic.linkWhiz_backend.repository.UserRepository;
import com.aktic.linkWhiz_backend.service.jwt.JwtService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final PlanRepository planRepository;

    @Value("${GOOGLE_CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String GOOGLE_CLIENT_SECRET;

    @Transactional
    public ResponseEntity<ApiResponse<AuthenticationResponse>> processOAuthLogin(String provider, String code) {
        try {
            // Exchange authorization code for access token
            ResponseEntity<Map> tokenResponse = getTokensFromGoogle(code);
            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Failed to retrieve access token", null));
            }

            Map<String, Object> tokenBody = tokenResponse.getBody();
            String accessToken = (String) tokenBody.get("access_token");

            // Fetch user information from Google
            ResponseEntity<Map> userInfoResponse = getUserInfoFromGoogle(accessToken);
            if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Failed to retrieve user info", null));
            }

            Map<String, Object> userInfo = userInfoResponse.getBody();
            System.out.println(userInfo);
            String email = (String) userInfo.get("email");
            String firstName = (String) userInfo.get("given_name");
            String lastName = (String) userInfo.get("family_name");

            // Check if user exists or create a new user
            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user;

            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            } else {
                Role userRole = roleRepository.findByRoleName("USER");
                if (userRole == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>(false, "Role not found", null));
                }

                Optional<Plan> plan = planRepository.findByPlanName("Free");
                if (plan.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>(false, "Plan not found", null));
                }

                user = User.builder()
                        .id(idGenerator.nextId())
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .plan(plan.get())
                        .role(userRole)
                        .provider(AuthProvider.valueOf(provider.toUpperCase()))
                        .build();
                userRepository.save(user);
            }

            // Generate JWT Token
            String token = jwtService.generateToken(user.getEmail());
            UserInfo userInfoDto = UserInfo.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .image(user.getImage())
                    .plan(user.getPlan())
                    .role(user.getRole().getRoleName())
                    .isActive(user.getIsActive())
                    .isVerified(user.getIsVerified())
                    .provider(user.getProvider().name())
                    .build();

            return ResponseEntity.ok(new ApiResponse<>(true, "OAuth login successful",
                    new AuthenticationResponse(userInfoDto, token)));

        } catch (Exception e) {
            log.error("Error occurred while processing OAuth login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error occurred while processing OAuth login", null));
        }
    }

    private ResponseEntity<Map> getTokensFromGoogle(String code) {
        String url = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "https://developers.google.com/oauthplayground");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(url, request, Map.class);
    }

    private ResponseEntity<Map> getUserInfoFromGoogle(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }


}
