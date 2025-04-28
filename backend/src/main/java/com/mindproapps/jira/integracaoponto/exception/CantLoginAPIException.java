package com.mindproapps.jira.integracaoponto.exception;

public class CantLoginAPIException extends Exception {
    public CantLoginAPIException() {
        super("Can't loggin to API");
    }
}
