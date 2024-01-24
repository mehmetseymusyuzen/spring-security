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
                .role(Role.USER)
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

        var jwtToken = jwtService.generateToken(user);

        this.revokeAllUserTokens(user);

        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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
