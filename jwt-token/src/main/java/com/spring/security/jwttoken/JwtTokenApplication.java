package com.spring.security.jwttoken;

import com.spring.security.jwttoken.model.dto.request.AuthenticationRequest;
import com.spring.security.jwttoken.model.dto.request.RegisterRequest;
import com.spring.security.jwttoken.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.spring.security.jwttoken.model.Role.ADMIN;

@SpringBootApplication
public class JwtTokenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtTokenApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(
            AuthenticationService service
    ) {
        return args -> {
            var adminRegisterRequest = RegisterRequest.builder()
                    .firstname("admin")
                    .lastname("admin")
                    .email("admin@mail.com")
                    .password("password")
                    .role(ADMIN)
                    .build();
            service.register(adminRegisterRequest);

            var adminAuthRequest = AuthenticationRequest.builder()
                    .email(adminRegisterRequest.getEmail())
                    .password(adminRegisterRequest.getPassword())
                    .build();

            System.out.println("admin token : " + service.authenticate(adminAuthRequest).getAccessToken());

/*            var managerRegisterRequest = RegisterRequest.builder()
                    .firstname("manager")
                    .lastname("manager")
                    .email("manager@mail.com")
                    .password("password")
                    .role(MANAGER)
                    .build();
            service.register(managerRegisterRequest);

            var managerAuthRequest = AuthenticationRequest.builder()
                    .email(managerRegisterRequest.getEmail())
                    .password(managerRegisterRequest.getPassword())
                    .build();

            System.out.println("manager token : " + service.authenticate(managerAuthRequest).getAccessToken());*/
        };
    }
}
