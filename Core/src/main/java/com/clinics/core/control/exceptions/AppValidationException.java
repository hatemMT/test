package com.clinics.core.control.exceptions;

import lombok.Getter;

import java.util.List;

public class AppValidationException extends RuntimeException {
    @Getter
    private List<ErrorModel> errors;

    public AppValidationException(String message, List<ErrorModel> errors) {
        super(message);
        this.errors = errors;
    }
}
