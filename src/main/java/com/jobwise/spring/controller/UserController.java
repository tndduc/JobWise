package com.jobwise.spring.controller;

import com.jobwise.spring.payload.request.PromoteRequest;
import com.jobwise.spring.payload.response.UserTokenResponse;
import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.service.AuthService;
import com.jobwise.spring.service.RefreshTokenService;
import com.jobwise.spring.service.UserService;
import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    final AuthService authService;
    final RefreshTokenService refreshTokenService;

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")

    @Operation(summary = "Returns all users with pagination", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<User>> listAll(Pageable pageable) {
        return ResponseEntity.ok(userService.listAll(pageable));
    }

    @GetMapping("/tokens")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Returns all users tokens with pagination", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<RefreshToken>> listAllTokens(Pageable pageable) {
        return ResponseEntity.ok(refreshTokenService.listAll(pageable));
    }

    @GetMapping("/my/tokens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")

    @Operation(summary = "Returns all user tokens with pagination", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<UserTokenResponse>> listMyAllTokens(Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(refreshTokenService.listAllByUser(pageable, userDetails.getId()));
    }

    @PatchMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")

    @Operation(summary = "Promote user to others roles", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "When user not found"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Void> promote(@Valid @RequestBody PromoteRequest request) {
        userService.promote(UUID.fromString(request.getUserId()), request.getRoles());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/logout/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")

    @Operation(summary = "User logout", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "When user not found"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Void> logout(@PathVariable UUID userId) {
        authService.logout(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
