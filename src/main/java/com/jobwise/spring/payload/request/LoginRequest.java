package com.jobwise.spring.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class LoginRequest {

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Email of the login.",
            example = "admin@mail.com", required = true)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Password of the login.",
            example = "admin123", required = true)
    private String password;

}
