package com.jobwise.spring.service;

import com.jobwise.spring.payload.UserMachineDetails;
import com.jobwise.spring.payload.request.LoginRequest;
import com.jobwise.spring.payload.request.SignupRequest;
import com.jobwise.spring.payload.request.TokenRefreshRequest;
import com.jobwise.spring.exception.TokenRefreshException;
import com.jobwise.spring.exception.UserAlreadyExistsException;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.RefreshToken;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.model.User;
import com.jobwise.spring.payload.response.JwtResponse;
import com.jobwise.spring.payload.response.TokenRefreshResponse;
import com.jobwise.spring.repository.UserRepository;
import com.jobwise.spring.security.jwt.JwtUtils;
import com.jobwise.spring.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final RoleService roleService;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final RefreshTokenService refreshTokenService;

    public JwtResponse signIn(LoginRequest loginRequest, UserMachineDetails userMachineDetails) {
        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            throw new IllegalArgumentException("Email does not exist.");
        }

        try {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.create(userDetails.getId(), userMachineDetails);

            return JwtResponse
                .builder()
                .user(userDetails)
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Incorrect password.");
        }
    }

    public User signUp(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException(signUpRequest.getEmail());
        }

        Set<Role> roles = Set.of(roleService.findByName(ERole.ROLE_USER));

        User user = User
                .builder()
                .first_name(signUpRequest.getFirst_name())
                .last_name(signUpRequest.getLast_name())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map((user) -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    public void logout(UUID userId) {
        refreshTokenService.deleteByUserId(userId);
    }



}
