package com.esa.main.exceptions;

public class ApiRestException extends RuntimeException {
    public ApiRestException(String message) {
        super(message);
    }

    public ApiRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
