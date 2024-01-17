package com.spring.security.jwttoken.repository;

import com.spring.security.jwttoken.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(final String email);

}
