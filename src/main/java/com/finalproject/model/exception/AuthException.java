package com.finalproject.model.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(String.format("exception.ms-keycloak-auth.%s", message));
    }
}
