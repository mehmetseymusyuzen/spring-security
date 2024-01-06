package com.spring.security.basicauth.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_MOD("MOD");

    private String value;

    public String getValue() {
        return value;
    }

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return name();
    }

}
