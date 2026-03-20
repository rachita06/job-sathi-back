package com.example.jobsathi.controller;

import com.example.jobsathi.exception.BadRequestException;
import com.example.jobsathi.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Created by Rabindra Adhikari on 2/16/26
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, WebRequest webRequest) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, exception.getMessage(), webRequest.getDescription(false), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException exception, WebRequest webRequest) {
        LOGGER.error("Invalid Request parameters : {}", exception.getMessage());
        return new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),null,exception.getMessage(), webRequest.getDescription(false),null);

    }

    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e,WebRequest webRequest){
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(),HttpStatus.UNAUTHORIZED.value(),null,e.getMessage(), webRequest.getDescription(false),null);
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);

    }
}