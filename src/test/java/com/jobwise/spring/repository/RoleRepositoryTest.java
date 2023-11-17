package com.jobwise.spring.repository;

import com.jobwise.spring.util.role.RoleCreator;
import com.jobwise.spring.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Tests for RefreshTokenRepository")
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("findByName Returns Role When Successful")
    void findByName_ReturnsRole_WhenSuccessful() {
        roleRepository.save(RoleCreator.createRoleToBeSave());

        Optional<Role> roleFound = roleRepository.findByName(RoleCreator.createRoleToBeSave().getName());

        assertThat(roleFound).isNotEmpty();

        assertThat(roleFound.get().getName()).isEqualTo(RoleCreator.createRoleToBeSave().getName());
    }

}
