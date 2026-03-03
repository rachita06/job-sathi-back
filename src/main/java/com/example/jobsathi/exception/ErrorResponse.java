package com.example.jobsathi.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public record ErrorResponse(LocalDateTime timestamp, int status, String exception, String message, String path,
                            List<CustomError> errors) {
}
