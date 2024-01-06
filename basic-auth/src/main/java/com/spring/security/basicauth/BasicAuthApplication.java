package com.spring.security.basicauth;

import com.spring.security.basicauth.dto.CreateUserRequest;
import com.spring.security.basicauth.model.Role;
import com.spring.security.basicauth.model.User;
import com.spring.security.basicauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class BasicAuthApplication implements CommandLineRunner {

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createDummyDate();
    }

    private void createDummyDate() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Sego")
                .username("sego21")
                .password("pass")
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        userService.createUser(request);

        CreateUserRequest request2 = CreateUserRequest.builder()
                .name("Memo")
                .username("memo21")
                .password("pass")
                .authorities(Set.of(Role.ROLE_MOD))
                .build();
        userService.createUser(request2);

        CreateUserRequest request3 = CreateUserRequest.builder()
                .name("Meso")
                .username("meso21")
                .password("pass")
                .authorities(Set.of(Role.ROLE_ADMIN))
                .build();
        userService.createUser(request3);

    }
}
