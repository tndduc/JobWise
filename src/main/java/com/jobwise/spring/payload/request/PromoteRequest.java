package com.jobwise.spring.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoteRequest {

    @Schema(description = "Roles to promote user")
    private Set<String> roles;

    private String userId;

}
