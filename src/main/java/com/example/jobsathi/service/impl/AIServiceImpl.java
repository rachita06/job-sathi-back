package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.dto.response.ResumeScoreResponseDTO;
import com.example.jobsathi.service.AIService;
import com.example.jobsathi.service.TFIDFAlgorithm;
import com.example.jobsathi.service.util.AIPromptBuilder;
import com.example.jobsathi.service.util.AIResponseParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.example.jobsathi.service.impl.DocumentExtractServiceUtil.getExtension;


/**
 * Created by Rabindra Adhikari on 2/28/26
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AIServiceImpl implements AIService {

    private static final String API_URL="https://router.huggingface.co/v1/chat/completions";
//    private static final String API_URL="https://router.huggingface.co/v1";
    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Value("${ai.openai.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AIResponseParser parser;
    private final TFIDFAlgorithm tfidfAlgorithm;
    @Override
    public ResumeScoreResponseDTO resumeAnalysis(MultipartFile pdfFile) {
        if (pdfFile.isEmpty()){
            return ResumeScoreResponseDTO.builder().build();
        }
        String extension=DocumentExtractServiceUtil.getExtension(pdfFile.getOriginalFilename());

        String extractText = DocumentExtractServiceUtil.extractText(pdfFile,extension);

        LOGGER.info("Calling OpenAI API (model={})...", model);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Build OpenAI-style messages for router
            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "You are an expert ATS resume analyser. Return only valid JSON."),
                    Map.of("role", "user", "content", AIPromptBuilder.build(extractText))
            );

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", messages,
                    "temperature", 0.2
            );

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String raw = response.getBody();
            LOGGER.info("HF responded ({} chars)", raw != null ? raw.length() : 0);
           AiAnalysisResumeResponseDTO aiResumeResponse= parser.parse(raw);

            return tfidfAlgorithm.useTFIDFAlgorithm(aiResumeResponse,extension,extractText);

        } catch (Exception e) {
            LOGGER.error("OpenAI API call failed: {}", e.getMessage(), e);
            throw new RuntimeException("OpenAI API error: " + e.getMessage(), e);
        }
    }
}
