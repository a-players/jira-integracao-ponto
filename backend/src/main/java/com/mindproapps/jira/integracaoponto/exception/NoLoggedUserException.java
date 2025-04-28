package com.mindproapps.jira.integracaoponto.exception;

public class NoLoggedUserException extends RuntimeException {
    public NoLoggedUserException() {
        super("No logged user");
    }
}
