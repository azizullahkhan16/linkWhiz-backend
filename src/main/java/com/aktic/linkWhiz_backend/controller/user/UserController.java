package com.aktic.linkWhiz_backend.controller.user;

import com.aktic.linkWhiz_backend.model.request.UpdateUserInfo;
import com.aktic.linkWhiz_backend.model.response.UserInfo;
import com.aktic.linkWhiz_backend.service.user.UserService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping(value = "/updateProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserInfo>> updateProfile(@ModelAttribute final UpdateUserInfo updateUserInfo) {
        return userService.updateProfile(updateUserInfo);
    }
}
