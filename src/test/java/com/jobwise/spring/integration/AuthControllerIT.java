package com.jobwise.spring.integration;

import com.jobwise.spring.exception.details.ExceptionDetails;
import com.jobwise.spring.payload.request.LoginRequest;
import com.jobwise.spring.payload.request.SignupRequest;
import com.jobwise.spring.payload.request.TokenRefreshRequest;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.payload.response.TokenRefreshResponse;
import com.jobwise.spring.repository.RefreshTokenRepository;
import com.jobwise.spring.repository.UserRepository;
import com.jobwise.spring.util.JWTCreator;
import com.jobwise.spring.util.auth.AuthCreator;
import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for AuthController")
public class AuthControllerIT {

    @Autowired
    TestRestTemplate httpClient;

    @Autowired
    RefreshTokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTCreator jwtCreator;

    @Test
    @DisplayName("signIn Returns JwtResponse When Successful")
    void signIn_ReturnsJwtResponse_WhenSuccessful() {
        LoginRequest login = LoginRequest
                .builder()
                .email("adam@gmail.com")
                .password("123456")
                .build();

        ResponseEntity<JwtResponse> entity = httpClient
                .postForEntity("/auth/signin", new HttpEntity<>(login), JwtResponse.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotNull();

    }

    @Test
    @DisplayName("signUp_SaveUser_WhenSuccessful")
    void signUp_SaveUser_WhenSuccessful() {
        SignupRequest signup = AuthCreator.createSignupRequest();

        ResponseEntity<User> entity = httpClient
                .postForEntity("/auth/signup", new HttpEntity<>(signup), User.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getEmail()).isEqualTo(signup.getEmail());
    }

    @Test
    @DisplayName("refreshToken Returns Token Refresh When Successful")
    void refreshToken_ReturnsTokenRefresh_WhenSuccessful() {
        jwtCreator.createAdminAuthEntity(null);

        String token = getAdminToken();

        TokenRefreshRequest request = TokenRefreshRequest
                .builder()
                .refreshToken(token)
                .build();

        ResponseEntity<TokenRefreshResponse> entity = httpClient.postForEntity("/auth/refresh", new HttpEntity<>(request), TokenRefreshResponse.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getRefreshToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        ResponseEntity<Void> entity = httpClient.exchange(
                "/auth/logout",
                HttpMethod.DELETE,
                jwtCreator.createAdminAuthEntity(null),
                Void.class
        );

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

    @Test
    @DisplayName("logout Returns 400 BadRequest When User Not Logged")
    void logout_Returns400BadRequest_WhenUserNotLogged() {
        ResponseEntity<ExceptionDetails> entity = httpClient.exchange(
                "/auth/logout",
                HttpMethod.DELETE,
                null,
                ExceptionDetails.class
        );

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getDetails()).isEqualTo("You are not logged in.");
    }

    private String getAdminToken() throws RuntimeException {
        User user = userRepository
                .findByEmail("admin@mail.com")
                .orElseThrow(RuntimeException::new);

        Page<RefreshToken> allByUser = tokenRepository.findAllByUser(PageRequest.of(0, 1), user);
        return allByUser.getContent().get(0).getToken();
    }

}
