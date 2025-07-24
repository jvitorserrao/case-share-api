package com.example.demo.api.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class WarningException extends RuntimeException {

    private final List<String> allErrors;
    public WarningException(String message) {
        super(message);
        allErrors = Collections.singletonList(message);
    }

    public WarningException(List<String> errors) {
        allErrors = errors;
    }
}
