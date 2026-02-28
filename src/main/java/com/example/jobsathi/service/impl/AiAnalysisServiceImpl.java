package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.service.AiAnalysisService;
import com.example.jobsathi.service.util.AIPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Slf4j
@RequiredArgsConstructor
public class AiAnalysisServiceImpl implements AiAnalysisService {
    @Override
    public AiAnalysisResumeResponseDTO analysis(String text) {
        String prompt=AIPromptBuilder.build(text);
        return AiAnalysisResumeResponseDTO.builder()
                .build();
    }
}
