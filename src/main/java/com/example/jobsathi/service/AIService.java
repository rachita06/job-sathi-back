package com.example.jobsathi.service;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;

/**
 * Created by Rabindra Adhikari on 2/28/26
 */
public interface AIService {
    AiAnalysisResumeResponseDTO analysis(String resumeText);
}
