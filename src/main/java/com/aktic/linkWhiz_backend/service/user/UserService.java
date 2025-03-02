package com.aktic.linkWhiz_backend.service.user;

import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.request.ChangePasswordRequest;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.repository.UserRepository;
import com.aktic.linkWhiz_backend.service.auth.AuthService;
import com.aktic.linkWhiz_backend.service.fileStorage.FileStorageService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public ResponseEntity<ApiResponse<UserInfo>> updateProfile(String firstName, String lastName, MultipartFile image) {
        try {
            User user = authService.getCurrentUser();

            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (lastName != null) {
                user.setLastName(lastName);
            }
            if (image != null) {
                if (user.getImage() != null) fileStorageService.delete(user.getImage());
                user.setImage(fileStorageService.save(image));
            }

            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", new UserInfo(user)));

        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<Resource> getProfileImage() {
        try {
            User user = authService.getCurrentUser();

            Resource resource = fileStorageService.load(user.getImage());
            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Detect the content type dynamically
            String contentType = Files.probeContentType(resource.getFile().toPath());

            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback for unknown files
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + user.getImage() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Unexpected error occurred while getting profile image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    public ResponseEntity<ApiResponse<String>> updatePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            User user = authService.getCurrentUser();

            if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "New password cannot be the same as the old password", null));
            }

            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid old password", null));
            }

            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "Password updated successfully", null));


        } catch (Exception e) {
            log.error("Unexpected error occurred while updating password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public User getUserInfo(String email) {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Exception e) {
            log.error("Unexpected error occurred while getting user info", e);
            return null;
        }
    }
}
