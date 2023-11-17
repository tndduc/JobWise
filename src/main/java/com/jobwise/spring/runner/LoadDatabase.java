package com.jobwise.spring.runner;

import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.property.DefaultDataProps;
import com.jobwise.spring.repository.RoleRepository;
import com.jobwise.spring.repository.UserRepository;
import com.jobwise.spring.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner createDefaultRolesIfNoneExists(RoleRepository roleRepository) {
        return (args) -> {
            if (roleRepository.count() > 0) {
                return;
            }

            Role userRole = Role
                    .builder()
                    .name(ERole.ROLE_USER)
                    .build();

            Role modRole = Role
                    .builder()
                    .name(ERole.ROLE_MODERATOR)
                    .build();

            Role adminRole = Role
                    .builder()
                    .name(ERole.ROLE_ADMIN)
                    .build();

            roleRepository.saveAll(List.of(userRole, modRole, adminRole));
        };
    }

    @Bean
    CommandLineRunner createDefaultUsersIfNoneExists(UserRepository userRepository, RoleRepository roleRepository, DefaultDataProps data) {
        return (args) -> {
            if (userRepository.count() > 0) {
                return;
            }

            Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);

            User adminUser = User
                    .builder()
                    .email(data.getUser().getEmail())
                    .username(data.getUser().getUsername())
                    .password(new BCryptPasswordEncoder().encode(data.getUser().getPassword()))
                    .roles(Set.of(adminRole.get()))
                    .build();

            userRepository.save(adminUser);
        };
    }

}
