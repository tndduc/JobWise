package com.jobwise.spring.payload.response;

import com.jobwise.spring.model.User;
import com.jobwise.spring.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
@Getter
@Setter
@NoArgsConstructor
@Builder
public class JwtResponse {

    @Schema(description = "Token to access protected endpoints")
    private String token;
    @Schema(description = "Type of token")
    private String type = "Bearer";
    @Schema(description = "Token to generate others access tokens")
    private String refreshToken;
    private UserDetailsImpl user;
//    private String id;
//    private String first_name;
//    private String last_name;
//
//    private String email;


    public JwtResponse(String accessToken, String refreshToken,UserDetailsImpl user) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
//        this.id = id;
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.email = email;

    }

    public JwtResponse(String accessToken, String type, String refreshToken, UserDetailsImpl user) {
        this(accessToken, refreshToken,user);

        if (type == null) {
            type = "Bearer";
        }

        this.type = type;
    }

}
