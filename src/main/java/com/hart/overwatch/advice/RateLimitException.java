package com.hart.overwatch.advice;

public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }
}

