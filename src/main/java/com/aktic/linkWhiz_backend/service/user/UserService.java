package com.aktic.linkWhiz_backend.service.user;

import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.request.UpdateUserInfo;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.repository.UserRepository;
import com.aktic.linkWhiz_backend.service.fileStorage.FileStorageService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ResponseEntity<ApiResponse<UserInfo>> updateProfile(UpdateUserInfo updateUserInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        try {
            Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null));
            }

            User user = optionalUser.get();

            if (updateUserInfo.getFirstName() != null) {
                user.setFirstName(updateUserInfo.getFirstName());
            }
            if (updateUserInfo.getLastName() != null) {
                user.setLastName(updateUserInfo.getLastName());
            }
            if (updateUserInfo.getImage() != null) {
                fileStorageService.delete(user.getImage());
                user.setImage(fileStorageService.save(updateUserInfo.getImage()));
            }

            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", new UserInfo(user)));

        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
