package com.example.jobsathi.controller;

import com.example.jobsathi.exception.BadRequestException;
import com.example.jobsathi.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handleBadRequestException(BadRequestException exception, WebRequest request) {
        LOGGER.info("Invalid Request parameters:{}", exception.getMessage());
        return new ErrorResponse(
                LocalDateTime.now(), exception.getStatus().value(), exception.getLocalizedMessage(),
                exception.getMessage(), request.getDescription(false), null);
    }
}
