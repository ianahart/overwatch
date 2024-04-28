package com.hart.overwatch.advice;

public class RedirectException extends RuntimeException {

    public RedirectException(String message) {
        super(message);
    }
}
