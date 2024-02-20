package com.spring.security.jwttoken.service;

import com.spring.security.jwttoken.model.Role;
import com.spring.security.jwttoken.model.Token;
import com.spring.security.jwttoken.model.TokenType;
import com.spring.security.jwttoken.model.User;
import com.spring.security.jwttoken.model.dto.request.AuthenticationRequest;
import com.spring.security.jwttoken.model.dto.request.RegisterRequest;
import com.spring.security.jwttoken.model.dto.response.AuthenticationResponse;
import com.spring.security.jwttoken.repository.TokenRepository;
import com.spring.security.jwttoken.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(
            final RegisterRequest registerRequest
    ) {
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(
            final AuthenticationRequest authenticationRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository
                .findByEmail(authenticationRequest.getEmail())
                .orElseThrow();

        this.revokeAllUserTokens(user);

        final String accessToken = jwtService
                .generateToken(user);

        saveUserToken(user, accessToken);

        final String refreshToken = jwtService
                .generateRefreshToken(user);

        saveUserToken(user, refreshToken);


        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessTokenUsingByRefreshToken(
            final HttpServletRequest request
    ) {

        final String refreshToken = jwtService.getJwtFromHeader(request);

        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {

            var user = this.userRepository
                    .findByEmail(userEmail)
                    .orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {


                final String accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);

                return accessToken;
            }
        }

        throw new RuntimeException();
    }

    private void revokeAllUserTokens(final User user) {
        var validUserToken = tokenRepository.findTokenByUserAndExpiredIsFalseAndRevokedIsFalse(user);
        if (validUserToken.isEmpty()) {
            return;
        }
        validUserToken.forEach(
                token -> {
                    token.setRevoked(true);
                    token.setExpired(true);
                }
        );
        tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

}
