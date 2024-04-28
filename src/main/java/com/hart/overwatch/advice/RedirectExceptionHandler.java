package com.hart.overwatch.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RedirectExceptionHandler {
    @ExceptionHandler(RedirectException.class)
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @ResponseBody
    public ErrorResponse handleRedirectException(BadRequestException ex) {

        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        return response;
    }

}

