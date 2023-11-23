package com.jobwise.spring.repository;

import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    Page<RefreshToken> findAllByUser(Pageable pageable, User user);

    @Modifying
    void deleteByUser(User user);
}
