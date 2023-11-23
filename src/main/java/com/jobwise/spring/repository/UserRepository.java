package com.jobwise.spring.repository;

import com.jobwise.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}
