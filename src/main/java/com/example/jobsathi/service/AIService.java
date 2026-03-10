package com.example.jobsathi.service;

import com.example.jobsathi.dto.response.ResumeScoreResponseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Rabindra Adhikari on 2/28/26
 */
public interface AIService {
    ResumeScoreResponseDTO resumeAnalysis(MultipartFile pdfFile);
}
