package com.example.demo.api.exception.advice;


import com.example.demo.api.exception.ApiErrorException;
import com.example.demo.api.exception.ApiErrors;
import com.example.demo.api.exception.ApiWarningException;
import com.example.demo.api.exception.Level;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Log
public class ApplicationControllerAdvice {

    @ExceptionHandler(ApiWarningException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleCsgWarningException(ApiWarningException ex) {
        return new ApiErrors(ex.getMessage(),  Level.WARNING);
    }

    @ExceptionHandler(ApiErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleCsgErrorException(ApiErrorException ex) {
        return new ApiErrors(ex.getMessage(),  Level.ERROR);
    }

}