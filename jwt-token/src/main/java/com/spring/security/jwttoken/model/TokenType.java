package com.spring.security.jwttoken.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {
    BEARER("Bearer");

    private final String value;
}
