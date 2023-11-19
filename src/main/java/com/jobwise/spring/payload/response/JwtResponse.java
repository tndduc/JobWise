package com.jobwise.spring.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

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
    private String id;
    private String first_name;
    private String last_name;

    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, String id, String first_name,String last_name, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse(String accessToken, String type, String refreshToken, String id, String first_name,String last_name, String email, List<String> roles) {
        this(accessToken, refreshToken, id, first_name,last_name, email, roles);

        if (type == null) {
            type = "Bearer";
        }

        this.type = type;
    }

}
