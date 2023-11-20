package com.jobwise.spring.util.auth;

import com.jobwise.spring.payload.request.LoginRequest;
import com.jobwise.spring.payload.request.SignupRequest;
import com.jobwise.spring.payload.request.TokenRefreshRequest;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.payload.response.TokenRefreshResponse;
import com.jobwise.spring.model.User;
import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.util.token.RefreshTokenCreator;
import com.jobwise.spring.util.user.UserCreator;

import java.util.List;

public class AuthCreator {

    public static final String FIRST_NAME = "adam";
    public static final String LAST_NAME = "tom";


    public static final String EMAIL = "adam@gmail.com";

    public static final String PASSWORD = "123456";
    public static final String TOKEN = "token-test";

    public static final User USER = UserCreator.createUser();

    public static LoginRequest createLoginRequest() {
        return LoginRequest
                .builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    public static SignupRequest createSignupRequest() {
        return SignupRequest
                .builder()
                .email(EMAIL)
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .password(PASSWORD)
                .build();
    }

    public static JwtResponse createJwtResponse() {
        return JwtResponse
                .builder()
                .token(TOKEN)
                .type("Bearer")
                .refreshToken(RefreshTokenCreator.TOKEN)
                .user(UserDetailsImpl.build(new User()))
//                .id(USER.getId().toString())
//                .first_name(USER.getFirst_name())
//                .last_name(USER.getLast_name())
//                .email(USER.getEmail())
//                .roles(List.of("ROLE_USER"))
                .build();
    }

    public static TokenRefreshRequest createTokenRefreshRequest() {
        return TokenRefreshRequest
                .builder()
                .refreshToken(RefreshTokenCreator.TOKEN)
                .build();
    }

    public static TokenRefreshResponse createTokenRefreshResponse() {
        return TokenRefreshResponse
                .builder()
                .accessToken(TOKEN)
                .refreshToken(RefreshTokenCreator.TOKEN)
                .tokenType("Bearer")
                .build();
    }

}
