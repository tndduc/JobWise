package com.jobwise.spring.controller;

import com.jobwise.spring.exception.BadRequestException;
import com.jobwise.spring.payload.UserMachineDetails;
import com.jobwise.spring.payload.request.LoginRequest;
import com.jobwise.spring.payload.request.SignupRequest;
import com.jobwise.spring.payload.request.TokenRefreshRequest;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.payload.response.TokenRefreshResponse;
import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.service.AuthService;
import com.jobwise.spring.model.User;
import com.jobwise.spring.util.HttpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final AuthService authService;

    @PostMapping("/sign-in")
    @Operation(summary = "User sign in", tags = "Auth")

    public ResponseEntity<?> signIn(
            @Valid @RequestBody
            @Parameter(description="Email and password to login. Cannot be empty.",
                    required=true)LoginRequest loginRequest,HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ipAddress = HttpUtils.getClientIp();

        UserMachineDetails userMachineDetails = UserMachineDetails
                .builder()
                .ipAddress(ipAddress)
                .browser(userAgent.getBrowser().getName())
                .operatingSystem(userAgent.getOperatingSystem().getName())
                .build();
        try {
            JwtResponse response = authService.signIn(loginRequest, userMachineDetails);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "User sign up", tags = "Auth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User Created"),
            @ApiResponse(responseCode = "400", description = "When email already exists"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<User> signUp(@Valid @RequestBody SignupRequest signUpRequest) {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    @Operation(summary = "User refresh token", tags = "Auth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "403", description = "When refresh token don't exists"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @DeleteMapping("/logout")
    @Operation(summary = "User logout", tags = "Auth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "When user not logged in"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (authentication instanceof AnonymousAuthenticationToken || principal == null) {
            throw new BadRequestException("You are not logged in.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;

        authService.logout(userDetails.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
