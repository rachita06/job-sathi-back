package com.example.jobsathi.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public interface DocumentExtractService {
    String extractText(MultipartFile file);
}
