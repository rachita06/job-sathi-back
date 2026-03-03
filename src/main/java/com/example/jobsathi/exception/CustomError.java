package com.example.jobsathi.exception;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public record CustomError(int errorCode, String field, String message) {
}
