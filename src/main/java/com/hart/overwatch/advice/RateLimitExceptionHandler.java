package com.hart.overwatch.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RateLimitExceptionHandler {
    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ResponseBody
    public ErrorResponse handleBadRequestException(RateLimitException ex) {

        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        return response;
    }

}

