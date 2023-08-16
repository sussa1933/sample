package com.study.project.auth;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String role) {
        this.role = role;
    }

    private String role;
}
