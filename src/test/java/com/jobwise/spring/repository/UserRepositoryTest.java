package com.jobwise.spring.repository;

import com.jobwise.spring.util.user.UserCreator;
import com.jobwise.spring.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@DataJpaTest
@DisplayName("Tests for UserRepository")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("findByEmail returns user when successful")
    void findByEmail_ReturnsUser_WhenSuccessful() {
        User userToBeSave = UserCreator.createUserToBeSave();

        userToBeSave.setRoles(new HashSet<>(roleRepository.saveAll(userToBeSave.getRoles())));

        userRepository.save(userToBeSave);

        Optional<User> userFound = userRepository.findByEmail(UserCreator.createUserToBeSave().getEmail());

        assertThat(userFound).isNotEmpty();

        assertThat(userFound.get()).isNotNull();

        assertThat(userFound.get().getEmail()).isEqualTo(UserCreator.createUserToBeSave().getEmail());
    }

    @Test
    @DisplayName("existsByEmail returns true when email already exists")
    void existsByEmail_ReturnsTrue_WhenEmailAlreadyExists() {
        User userToBeSave = UserCreator.createUserToBeSave();

        userToBeSave.setRoles(new HashSet<>(roleRepository.saveAll(userToBeSave.getRoles())));

        userRepository.save(userToBeSave);

        boolean userExists = userRepository.existsByEmail(UserCreator.createUserToBeSave().getEmail());

        assertThat(userExists).isTrue();
    }

    @Test
    @DisplayName("existsByEmail returns true when email don't exists")
    void existsByEmail_ReturnsFalse_WhenEmailDoNotExists() {
        boolean userExists = userRepository.existsByEmail(UserCreator.createUserToBeSave().getEmail());

        assertThat(userExists).isFalse();
    }

}
