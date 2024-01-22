package com.spring.security.jwttoken.contorller;

import com.spring.security.jwttoken.model.dto.request.RegisterRequest;
import com.spring.security.jwttoken.model.dto.request.AuthenticationRequest;
import com.spring.security.jwttoken.model.dto.response.AuthenticationResponse;
import com.spring.security.jwttoken.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody final RegisterRequest registerRequest
    ) {
        authenticationService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody final AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
