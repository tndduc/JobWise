package com.jobwise.spring.util.token;

import com.jobwise.spring.payload.response.UserTokenResponse;
import com.jobwise.spring.util.user.UserCreator;
import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
public class RefreshTokenCreator {

    public static final UUID ID = UUID.fromString("b11b35d6-ad2c-4aa9-87b4-a58cea010f0a");
    public static final String TOKEN = "refresh-token-test";
    public static final Instant EXPIRY_DATE = Instant.now().plus(1, ChronoUnit.HOURS);
    public static final User USER = UserCreator.createUserToBeSave();

    public static final String BROWSER = "browser-test";
    public static final String OPERATING_SYSTEM = "os-test";
    public static final String ID_ADDRESS = "ip-test";

    public static RefreshToken createRefreshTokenToBeSave() {
        return RefreshToken
                .builder()
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .user(USER)
                .browser(BROWSER)
                .operatingSystem(OPERATING_SYSTEM)
                .ipAddress(ID_ADDRESS)
                .build();
    }

    public static RefreshToken createRefreshToken() {
        return RefreshToken
                .builder()
                .id(ID)
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .user(USER)
                .browser(BROWSER)
                .operatingSystem(OPERATING_SYSTEM)
                .ipAddress(ID_ADDRESS)
                .build();
    }

    public static UserTokenResponse createUserTokenResponse() {
        return UserTokenResponse
                .builder()
                .id(ID.toString())
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .browser(BROWSER)
                .operatingSystem(OPERATING_SYSTEM)
                .ipAddress(ID_ADDRESS)
                .build();
    }

}
