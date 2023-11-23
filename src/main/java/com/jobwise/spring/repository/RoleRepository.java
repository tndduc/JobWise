package com.jobwise.spring.repository;

import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);
}
