package com.jobwise.spring.util;

import com.jobwise.spring.payload.request.LoginRequest;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.repository.RoleRepository;
import com.jobwise.spring.repository.UserRepository;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@Component
public class JWTCreator {
    @Autowired
    final TestRestTemplate testRestTemplate;

    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    final UserRepository userRepository;

    public JWTCreator(TestRestTemplate testRestTemplate, UserRepository userRepository, RoleRepository roleRepository) {
        this.testRestTemplate = testRestTemplate;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();

        persistUsers(roleRepository, userRepository);
    }

    public User createUser() {
        return User
                .builder()
                .first_name("User")
                .last_name("Panda")
                .email("user@mail.com")
                .password(passwordEncoder.encode("password"))
                .build();
    }

    public User createModerator() {
        return User
                .builder()
                .first_name("Moderator")
                .last_name("Phan")
                .email("mod@mail.com")
                .password(passwordEncoder.encode("password"))
                .build();
    }

    public User createAdmin() {
        return User
                .builder()
                .first_name("Admin")
                .last_name("Happ")
                .email("admin@mail.com")
                .password(passwordEncoder.encode("password"))
                .build();
    }

    public void persistUsers(RoleRepository roleRepository, UserRepository userRepository) {
        Role adminRole = roleRepository.save(new Role(ERole.ROLE_ADMIN));
        Role modRole = roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        Role userRole = roleRepository.save(new Role(ERole.ROLE_USER));

        User user = createUser();
        User moderator = createModerator();
        User admin = createAdmin();

        user.setRoles(Set.of(userRole));
        moderator.setRoles(Set.of(modRole));
        admin.setRoles(Set.of(adminRole));

        userRepository.saveAll(List.of(admin, user, moderator));
    }

    public <T> HttpEntity<T> createAdminAuthEntity(T t) {
        return createAuthEntity(t, new LoginRequest("admin@mail.com", "password"));
    }

    public <T> HttpEntity<T> createModeratorAuthEntity(T t) {
        return createAuthEntity(t, new LoginRequest("mod@mail.com", "password"));
    }

    public <T> HttpEntity<T> createUserAuthEntity(T t) {
        return createAuthEntity(t, new LoginRequest("user@mail.com", "password"));
    }

    public <T> HttpEntity<T> createAuthEntity(T t, LoginRequest login) {
        ResponseEntity<JwtResponse> entity = testRestTemplate
                .postForEntity("/auth/signin", new HttpEntity<>(login), JwtResponse.class);

        if (entity.getBody() != null && entity.getBody().getToken().isEmpty()) {
            throw new RuntimeException("Empty access token.");
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(entity.getBody().getToken());

        return new HttpEntity<>(t, headers);
    }

}