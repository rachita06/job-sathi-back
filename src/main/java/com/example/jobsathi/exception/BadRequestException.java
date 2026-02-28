package com.example.jobsathi.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, HttpStatus status) {
        super(message, status);
    }

    public BadRequestException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }

}
