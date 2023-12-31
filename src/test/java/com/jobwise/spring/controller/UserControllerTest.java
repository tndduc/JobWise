package com.jobwise.spring.controller;

import com.jobwise.spring.payload.response.UserTokenResponse;
import com.jobwise.spring.service.AuthService;
import com.jobwise.spring.service.RefreshTokenService;
import com.jobwise.spring.service.UserService;
import com.jobwise.spring.util.MockUtils;
import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static com.jobwise.spring.util.token.RefreshTokenCreator.createRefreshToken;
import static com.jobwise.spring.util.token.RefreshTokenCreator.createUserTokenResponse;
import static com.jobwise.spring.util.user.UserCreator.createPromoteRequest;
import static com.jobwise.spring.util.user.UserCreator.createUser;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for UserController")
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    AuthService authService;

    @Mock
    RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        PageImpl<User> usersPage = new PageImpl<>(List.of(
                createUser()
        ));

        PageImpl<RefreshToken> refreshTokensPage = new PageImpl<>(List.of(
                createRefreshToken()
        ));

        PageImpl<UserTokenResponse> userRefreshTokensPage = new PageImpl<>(List.of(
                createUserTokenResponse()
        ));

        BDDMockito
                .when(userService.listAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(usersPage);

        BDDMockito
                .when(refreshTokenService.listAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(refreshTokensPage);

        BDDMockito
                .when(refreshTokenService.listAllByUser(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.any(UUID.class)))
                .thenReturn(userRefreshTokensPage);

        BDDMockito
                .doNothing()
                .when(userService)
                .promote(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any());

        BDDMockito
                .doNothing()
                .when(authService)
                .logout(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("listAll Returns List Of Users Inside Page Object When Successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        User expectedUser = createUser();

        ResponseEntity<Page<User>> entity = userController.listAll(PageRequest.of(0, 1));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody())
                .isNotEmpty()
                .contains(expectedUser);
    }

    @Test
    @DisplayName("listAllTokens Returns List Of Refresh Tokens Inside Page Object When Successful")
    void listAllTokens_ReturnsListOfRefreshTokensInsidePageObject_WhenSuccessful() {
        RefreshToken expectedRefreshToken = createRefreshToken();

        ResponseEntity<Page<RefreshToken>> entity = userController.listAllTokens(PageRequest.of(0, 1));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody())
                .isNotEmpty()
                .contains(expectedRefreshToken);
    }

    @Test
    @DisplayName("listMyAllTokens Returns List Of Refresh Tokens Inside Page Object When Successful")
    void listMyAllTokens_ReturnsListOfRefreshTokensInsidePageObject_WhenSuccessful() {
        MockUtils.mockSecurityContextHolder();

        UserTokenResponse expectedRefreshToken = createUserTokenResponse();

        ResponseEntity<Page<UserTokenResponse>> entity = userController.listMyAllTokens(PageRequest.of(0, 1));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotEmpty();

        assertThat(entity.getBody().getContent())
                .isNotEmpty()
                .hasSize(1);

        assertThat(entity.getBody().getContent().get(0)).isNotNull();

        assertThat(entity.getBody().getContent().get(0).getId()).isEqualTo(expectedRefreshToken.getId());

        assertThat(entity.getBody().getContent().get(0).getToken()).isEqualTo(expectedRefreshToken.getToken());
    }

    @Test
    @DisplayName("promote Updates User Roles When Successful")
    void promote_UpdatesUserRoles_WhenSuccessful() {
        ResponseEntity<Void> entity = userController.promote(createPromoteRequest());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        ResponseEntity<Void> entity = userController.logout(UUID.randomUUID());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

}
