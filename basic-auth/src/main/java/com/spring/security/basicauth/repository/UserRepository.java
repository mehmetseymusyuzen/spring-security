package com.spring.security.basicauth.repository;

import com.spring.security.basicauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(final String userName);

}
