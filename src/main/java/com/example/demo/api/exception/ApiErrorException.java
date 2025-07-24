package com.example.demo.api.exception;

import java.util.List;

public class ApiErrorException extends WarningException {
    public ApiErrorException(String message) {
        super(message);
    }

    public ApiErrorException(List<String> errors) {
        super(errors);
    }
}
