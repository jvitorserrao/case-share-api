package com.example.demo.api.exception;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;

@Getter
@Log
public class ApiErrors {

    private final List<String> errors;

    public ApiErrors(List<String> errors, Level level){
        errors.forEach(level::gravarLog);
        errors = errors.stream().map(level::addLevel).toList();
        this.errors = errors;
    }

    public ApiErrors(String message, Level level){
        level.gravarLog(message);
        message = level.addLevel(message);
        this.errors = Collections.singletonList(message);
    }
}