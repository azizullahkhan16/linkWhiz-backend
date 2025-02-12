package com.aktic.linkWhiz_backend.controller.user;

import com.aktic.linkWhiz_backend.model.request.ChangePasswordRequest;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.service.user.UserService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping(value = "/updateProfile")
    public ResponseEntity<ApiResponse<UserInfo>> updateProfile(@RequestParam(required = false) String firstName,
                                                               @RequestParam(required = false) String lastName,
                                                               @RequestParam(required = false) MultipartFile image) {
        return userService.updateProfile(firstName, lastName, image);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Resource> getProfileImage() {
        return userService.getProfileImage();
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<ApiResponse<String>> updatePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return userService.updatePassword(changePasswordRequest);
    }
}
