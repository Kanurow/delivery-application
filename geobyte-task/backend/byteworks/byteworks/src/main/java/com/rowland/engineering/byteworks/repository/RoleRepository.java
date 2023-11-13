package com.rowland.engineering.byteworks.repository;

import com.rowland.engineering.byteworks.model.Role;
import com.rowland.engineering.byteworks.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
