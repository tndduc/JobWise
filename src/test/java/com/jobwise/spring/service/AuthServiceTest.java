package com.jobwise.spring.service;

import com.jobwise.spring.exception.TokenRefreshException;
import com.jobwise.spring.exception.UserAlreadyExistsException;
import com.jobwise.spring.payload.UserMachineDetails;
import com.jobwise.spring.payload.request.SignupRequest;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.payload.response.TokenRefreshResponse;
import com.jobwise.spring.repository.UserRepository;
import com.jobwise.spring.security.jwt.JwtUtils;
import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.util.GenericCreator;
import com.jobwise.spring.util.auth.AuthCreator;
import com.jobwise.spring.util.auth.UserDetailsImplCreator;
import com.jobwise.spring.util.role.RoleCreator;
import com.jobwise.spring.util.token.RefreshTokenCreator;
import com.jobwise.spring.util.user.UserCreator;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for AuthService")
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);

        BDDMockito
                .when(authenticationMock.getPrincipal())
                .thenReturn(UserDetailsImplCreator.createUserDetails());

        BDDMockito
                .when(authenticationManager.authenticate(ArgumentMatchers.any(Authentication.class)))
                .thenReturn(authenticationMock);

        BDDMockito
                .when(jwtUtils.generateJwtToken(ArgumentMatchers.any(UserDetailsImpl.class)))
                .thenReturn(AuthCreator.TOKEN);

        BDDMockito
                .when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(UserCreator.createUser());

        BDDMockito
                .when(refreshTokenService.create(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(UserMachineDetails.class)))
                .thenReturn(RefreshTokenCreator.createRefreshToken());

        BDDMockito
                .when(userRepository.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(false);

        BDDMockito
                .when(roleService.findByName(ArgumentMatchers.any(ERole.class)))
                .thenReturn(RoleCreator.createRole());

        BDDMockito
                .when(passwordEncoder.encode(ArgumentMatchers.any(CharSequence.class)))
                .thenReturn(AuthCreator.PASSWORD);

        BDDMockito
                .when(refreshTokenService.findByToken(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(RefreshTokenCreator.createRefreshToken()));

        BDDMockito
                .when(refreshTokenService.verifyExpiration(ArgumentMatchers.any(RefreshToken.class)))
                .thenReturn(RefreshTokenCreator.createRefreshToken());

        BDDMockito
                .when(jwtUtils.generateTokenFromEmail(ArgumentMatchers.anyString()))
                .thenReturn(AuthCreator.TOKEN);

        BDDMockito
                .doNothing()
                .when(refreshTokenService)
                .deleteByUserId(ArgumentMatchers.any(UUID.class));
    }

//    @Test
//    @DisplayName("signIn Authenticate And Returns Jwt Response When Successful")
//    void signIn_AuthenticateAndReturnsJwtResponse_WhenSuccessful() {
//        JwtResponse expectedResponse = AuthCreator.createJwtResponse();
//
//        JwtResponse jwtResponse = authService.signIn(AuthCreator.createLoginRequest(), GenericCreator.createUserMachineDetails());
//
//        assertThat(jwtResponse.getToken()).isEqualTo(expectedResponse.getToken());
//    }

    @Test
    @DisplayName("signUp Persists User When Successful")
    void signUp_PersistsUser_WhenSuccessful() {
        SignupRequest expectedUser = AuthCreator.createSignupRequest();

        User userCreated = authService.signUp(expectedUser);

        assertThat(userCreated).isNotNull();

        assertThat(userCreated.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    @DisplayName("signUp Throws UserAlreadyExistsException When User Already Exists")
    void signUp_ThrowsUserAlreadyExistsException_WhenUserAlreadyExists() {
        BDDMockito
                .when(userRepository.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(true);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> authService.signUp(AuthCreator.createSignupRequest()));
    }

    @Test
    @DisplayName("refreshToken Returns TokenRefreshResponse When Successful")
    void refreshToken_ReturnsTokenRefreshResponse_WhenSuccessful() {
        TokenRefreshResponse expectedResponse = AuthCreator.createTokenRefreshResponse();

        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(AuthCreator.createTokenRefreshRequest());

        assertThat(tokenRefreshResponse).isNotNull();

        assertThat(tokenRefreshResponse.getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken());

        assertThat(tokenRefreshResponse.getAccessToken()).isEqualTo(expectedResponse.getAccessToken());
    }

    @Test
    @DisplayName("refreshToken Throws TokenRefreshException When Refresh Token Not Found")
    void refreshToken_ThrowsTokenRefreshException_WhenRefreshTokenNotFound() {
        BDDMockito
                .when(refreshTokenService.findByToken(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(TokenRefreshException.class)
                .isThrownBy(() -> authService.refreshToken(AuthCreator.createTokenRefreshRequest()));
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        assertThatCode(() -> authService.logout(UUID.randomUUID()))
                .doesNotThrowAnyException();
    }

}
