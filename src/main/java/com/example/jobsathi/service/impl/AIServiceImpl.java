package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.service.AIService;
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

import java.util.List;
import java.util.Map;


/**
 * Created by Rabindra Adhikari on 2/28/26
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AIServiceImpl implements AIService {

    private static final String API_URL="https://router.huggingface.co/v1/chat/completions1";
    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Value("${ai.openai.model:katanemo/Arch-Router-1.5B:hf-inference}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AIResponseParser parser;
    @Override
    public AiAnalysisResumeResponseDTO analysis(String resumeText) {
        LOGGER.info("Calling OpenAI API (model={})...", model);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", "You are an expert ATS resume analyser. Return only valid JSON."),
                    Map.of("role", "user", "content", AIPromptBuilder.build(resumeText))
            );

            Map<String, Object> body = Map.of(
                    "model", model,   // Uses your @Value property
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

            return parser.parse(raw);

        } catch (Exception e) {
            LOGGER.error("OpenAI API call failed: {}", e.getMessage(), e);
            throw new RuntimeException("OpenAI API error: " + e.getMessage(), e);
        }
    }

    private String extractOpenAiText(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        return root.path("choices").get(0).path("message").path("content").asText();
    }
}
