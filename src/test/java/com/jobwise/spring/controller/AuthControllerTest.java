package com.jobwise.spring.controller;

import com.jobwise.spring.exception.BadRequestException;
import com.jobwise.spring.payload.UserMachineDetails;
import com.jobwise.spring.payload.request.LoginRequest;
import com.jobwise.spring.payload.request.SignupRequest;
import com.jobwise.spring.payload.request.TokenRefreshRequest;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.payload.response.TokenRefreshResponse;
import com.jobwise.spring.service.AuthService;
import com.jobwise.spring.util.MockUtils;
import com.jobwise.spring.util.auth.AuthCreator;
import com.jobwise.spring.util.user.UserCreator;
import com.jobwise.spring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for AuthController")
public class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    AuthService authService;

    @BeforeEach
    void setUp() {
        BDDMockito
                .when(authService.signIn(ArgumentMatchers.any(LoginRequest.class), ArgumentMatchers.any(UserMachineDetails.class)))
                .thenReturn(AuthCreator.createJwtResponse());

        BDDMockito
                .when(authService.signUp(ArgumentMatchers.any(SignupRequest.class)))
                .thenReturn(UserCreator.createUser());

        BDDMockito
                .when(authService.refreshToken(ArgumentMatchers.any(TokenRefreshRequest.class)))
                .thenReturn(AuthCreator.createTokenRefreshResponse());

        BDDMockito
                .doNothing()
                .when(authService)
                .logout(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("signIn Returns JwtResponse When Successful")
    void signIn_ReturnsJwtResponse_WhenSuccessful() {
        JwtResponse expectedResponse = AuthCreator.createJwtResponse();

        HttpServletRequest httpServletRequest = MockUtils.mockUserMachineInfo();

        ResponseEntity<?> entity = authController.signIn(AuthCreator.createLoginRequest(), httpServletRequest);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotNull();
;
    }

    @Test
    @DisplayName("signUp_SaveUser_WhenSuccessful")
    void signUp_SaveUser_WhenSuccessful() {
        SignupRequest expectedUser = AuthCreator.createSignupRequest();

        ResponseEntity<User> entity = authController.signUp(expectedUser);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    @DisplayName("refreshToken Returns Token Refresh When Successful")
    void refreshToken_ReturnsTokenRefresh_WhenSuccessful() {
        TokenRefreshResponse expectedResponse = AuthCreator.createTokenRefreshResponse();

        ResponseEntity<TokenRefreshResponse> entity = authController.refreshToken(AuthCreator.createTokenRefreshRequest());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getAccessToken()).isEqualTo(expectedResponse.getAccessToken());

        assertThat(entity.getBody().getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken());
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        MockUtils.mockSecurityContextHolder();

        ResponseEntity<Void> entity = authController.logout();

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

    @Test
    @DisplayName("logout Throws BadRequestException When User Not Logged")
    void logout_ThrowsBadRequestException_WhenUserNotLogged() {
        MockUtils.mockSecurityContextHolder(true);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> authController.logout());
    }

}
