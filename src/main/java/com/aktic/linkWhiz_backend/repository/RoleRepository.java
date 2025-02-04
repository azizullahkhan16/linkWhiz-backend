package com.aktic.linkWhiz_backend.repository;

import com.aktic.linkWhiz_backend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
