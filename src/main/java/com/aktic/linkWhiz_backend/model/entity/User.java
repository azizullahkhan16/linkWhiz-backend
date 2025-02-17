package com.aktic.linkWhiz_backend.model.entity;

import com.aktic.linkWhiz_backend.model.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, updatable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShortUrl> shortUrls;

    @PrePersist
    public void prePersist() {
        this.isVerified = this.isVerified != null && this.isVerified;
        this.isActive = this.isActive == null || this.isActive;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isVerified=" + isVerified +
                ", isActive=" + isActive +
                ", plan=" + plan +
                ", role=" + role +
                '}';
    }

}
