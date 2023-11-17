package com.jobwise.spring.service;

import com.jobwise.spring.exception.ResourceNotFoundException;
import com.jobwise.spring.repository.RoleRepository;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.jobwise.spring.util.role.RoleCreator.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for RoleService")
public class RoleServiceTest {

    @InjectMocks
    RoleService roleService;

    @Mock
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        BDDMockito
                .when(roleRepository.findByName(ArgumentMatchers.any(ERole.class)))
                .thenReturn(Optional.of(createRole()));
    }

    @Test
    @DisplayName("findByName Returns Role When Successful")
    void findByName_ReturnsRole_WhenSuccessful() {
        Role expectedRole = createRole();

        Role roleFound = roleService.findByName(ERole.ROLE_USER);

        assertThat(roleFound).isEqualTo(expectedRole);
    }

    @Test
    @DisplayName("findByName Throws ResourceNotFoundException When Role Not Found")
    void findByName_ThrowsResourceNotFoundException_WhenRoleNotFound() {
        BDDMockito
                .when(roleRepository.findByName(ArgumentMatchers.any(ERole.class)))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
                () -> roleService.findByName(ERole.ROLE_USER)
        );
    }

}
