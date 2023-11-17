package com.jobwise.spring.repository;

import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);
}
