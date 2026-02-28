package com.example.jobsathi.service;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public interface AiAnalysisService {

    AiAnalysisResumeResponseDTO analysis(String text);
}
