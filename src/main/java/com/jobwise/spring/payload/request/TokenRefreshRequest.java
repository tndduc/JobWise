package com.jobwise.spring.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshRequest {

    @NotBlank
    @Schema(description = "Refresh Token to generate more tokens")
    private String refreshToken;

}
