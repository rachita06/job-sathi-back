package com.example.jobsathi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by Rabindra Adhikari on 2/16/26
 */
@Getter
public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
    private final Throwable cause;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
        this.cause = null;
    }

    public CustomException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
        this.cause = cause;
    }
}
