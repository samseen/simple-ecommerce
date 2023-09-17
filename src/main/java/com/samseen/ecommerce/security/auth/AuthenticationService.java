package com.samseen.ecommerce.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samseen.ecommerce.enums.Role;
import com.samseen.ecommerce.security.CustomUserDetailsService;
import com.samseen.ecommerce.security.JWTService;
import com.samseen.ecommerce.token.entity.Token;
import com.samseen.ecommerce.token.entity.TokenType;
import com.samseen.ecommerce.token.repository.RoleRepository;
import com.samseen.ecommerce.token.repository.TokenRepository;
import com.samseen.ecommerce.user.User;
import com.samseen.ecommerce.user.UserRepository;
import com.samseen.ecommerce.user.entity.Permission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    public AuthenticationResponse register(RegisterRequest request) {

        Permission userPermission = roleRepository.getRoleByName("USER");
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userName(request.getEmail())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .updatedBy("SYSTEM")
                .createdBy("SYSTEM")
                .permissions(Set.of(userPermission))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(getUser(savedUser));
        var refreshToken = jwtService.generateRefreshToken(getUser(savedUser));

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserDetails getUser(User user) {
        return customUserDetailsService.loadUserByUsername(user.getEmail());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var user = userRepository.findByUserName(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(getUser(user));
        var refreshToken = jwtService.generateRefreshToken(getUser(user));
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByUserName(userEmail)
                    .orElseThrow();

            if (jwtService.isTokenValid(refreshToken, getUser(user))) {
                var accessToken = jwtService.generateToken(getUser(user));
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }
}
