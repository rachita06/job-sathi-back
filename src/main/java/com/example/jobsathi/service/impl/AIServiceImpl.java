package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.dto.response.ChatResponseDTO;
import com.example.jobsathi.dto.response.ResumeScoreResponseDTO;
import com.example.jobsathi.entity.ResumeEntity;
import com.example.jobsathi.repository.ResumeRepository;
import com.example.jobsathi.service.AIService;
import com.example.jobsathi.service.AuthenticatedUserService;
import com.example.jobsathi.service.TFIDFAlgorithm;
import com.example.jobsathi.service.util.AIPromptBuilder;
import com.example.jobsathi.service.util.AIResponseParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by Rabindra Adhikari on 2/28/26
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AIServiceImpl implements AIService {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    @Value("${ai.openai.api-key}")
    private String apiKey;
    @Value("${ai.openai.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AIResponseParser parser;
    private final TFIDFAlgorithm tfidfAlgorithm;
    private final ResumeRepository resumeRepository;
    private final AuthenticatedUserService authenticatedUserService;

    // TODO refactor to common  AI model calling
    @Override
    public ResumeScoreResponseDTO resumeAnalysis(MultipartFile pdfFile) {
        if (pdfFile.isEmpty()) {
            return ResumeScoreResponseDTO.builder().build();
        }
        String originalFileName = pdfFile.getOriginalFilename();
        String extension = DocumentExtractServiceUtil.getExtension(originalFileName);

        String extractText = DocumentExtractServiceUtil.extractText(pdfFile, extension);

        // saving resume
        try {
            String filePath = AIServiceImplUtil.saveFileToDir(originalFileName, pdfFile);
            ResumeEntity resume = new ResumeEntity();
            resume.setOriginalFileName(originalFileName);
            resume.setFilePath(filePath);
            resume.setFileType(extension);
            resume.setFileSize(pdfFile.getSize());
            resume.setUser(authenticatedUserService.getLoggedInUser());
            resumeRepository.save(resume);
        } catch (IOException e) {
            LOGGER.error("Error while saving file to local dir {}", e.getMessage());
        }

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
            AiAnalysisResumeResponseDTO aiResumeResponse = parser.parse(raw);

            return tfidfAlgorithm.useTFIDFAlgorithm(aiResumeResponse, extension, extractText);

        } catch (Exception e) {
            LOGGER.error("OpenAI API call failed: {}", e.getMessage(), e);
            throw new RuntimeException("OpenAI API error: " + e.getMessage(), e);
        }
    }

    @Override
    public ChatResponseDTO simpleChat(String chat) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Build OpenAI-style messages for router
            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "Try to sound as accurate as possible"),
                    Map.of("role", "user", "content", chat)
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
            String userResponse = AIResponseParser.jsonToString(raw, objectMapper);
            if (userResponse == null) {
                return new ChatResponseDTO("Sorry, cannot generate response", false);
            }
            LOGGER.info("HF responded ({} chars)", raw != null ? raw.length() : 0);
            return new ChatResponseDTO(userResponse, true);
        } catch (Exception e) {
            LOGGER.error("OpenAI API call failed: {}", e.getMessage(), e);
            //TODO throw the ex here
            return new ChatResponseDTO("Sorry for the delay. Please try again letter", false);
        }
    }
}
