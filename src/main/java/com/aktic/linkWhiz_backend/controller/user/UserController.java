package com.aktic.linkWhiz_backend.controller.user;

import com.aktic.linkWhiz_backend.model.request.UpdateUserInfo;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.service.user.UserService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping(value = "/updateProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserInfo>> updateProfile(@ModelAttribute final UpdateUserInfo updateUserInfo) {
        return userService.updateProfile(updateUserInfo);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Resource> getProfileImage() {
        return userService.getProfileImage();
    }
}
