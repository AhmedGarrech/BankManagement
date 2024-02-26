package com.garrech.bankmanagement.configurations.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CrudException extends RuntimeException {
    private final String errorMessage;
    private final HttpStatus errorCode;

    public CrudException(String errorMessage, HttpStatus errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
