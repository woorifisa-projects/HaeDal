package com.haedal.backend.auth.exception;


public class InactiveUserException extends RuntimeException {
    public InactiveUserException(String message) {
        super(message);
    }
}