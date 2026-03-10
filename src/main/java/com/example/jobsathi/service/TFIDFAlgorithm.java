package com.example.jobsathi.service;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.dto.response.ResumeScoreResponseDTO;

/**
 * Created by Rabindra Adhikari on 3/1/26
 */
public interface TFIDFAlgorithm {

    ResumeScoreResponseDTO useTFIDFAlgorithm(AiAnalysisResumeResponseDTO ai, String fileType,
                                             String rawText);

}
