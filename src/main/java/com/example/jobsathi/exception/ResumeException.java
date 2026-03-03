package com.example.jobsathi.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public class ResumeException extends CustomException{
    public ResumeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
