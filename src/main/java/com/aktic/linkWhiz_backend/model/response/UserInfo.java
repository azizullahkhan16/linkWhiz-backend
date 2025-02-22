package com.aktic.linkWhiz_backend.model.response;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isActive;
    private Boolean isVerified;
    private String image;
    private String createdAt;
    private String updatedAt;
    private Plan plan;
    private String role;
    private String provider;

    public UserInfo(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.isActive = user.getIsActive();
        this.isVerified = user.getIsVerified();
        this.image = user.getImage();
        this.createdAt = user.getCreatedAt().toString();
        this.updatedAt = user.getUpdatedAt().toString();
        this.plan = user.getPlan();
        this.role = user.getRole().getRoleName();
    }
}
