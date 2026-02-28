package com.example.jobsathi.service.util;

import com.example.jobsathi.dto.GrammarIssue;
import com.example.jobsathi.dto.WeakPhrase;
import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rabindra Adhikari on 2/27/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AIResponseParser {
    private final ObjectMapper objectMapper;

    public AiAnalysisResumeResponseDTO parse(String rawJson) throws JsonProcessingException {
        String clean = rawJson.trim();

        JsonNode outer = objectMapper.readTree(clean);

        String content = outer
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();
        content = content
                .replace("'", "\"")
                .replace("True", "true")
                .replace("False", "false")
                .replace("None", "null");

        try {
            JsonNode root = objectMapper.readTree(content);
            return AiAnalysisResumeResponseDTO.builder()
                    // Dimension scores
                    .grammarScore(getDoubleResponse(root, "grammarScore"))
                    .formatScore(getDoubleResponse(root, "formatScore"))
                    .skillScore(getDoubleResponse(root, "skillScore"))
                    .keywordScore(getDoubleResponse(root, "keywordScore"))
                    .experienceScore(getDoubleResponse(root, "experienceScore"))
                    .educationScore(getDoubleResponse(root, "educationScore"))
                    .contactScore(getDoubleResponse(root, "contactScore"))
                    .verbDiversityScore(getDoubleResponse(root, "verbDiversityScore"))
                    .readabilityScore(getDoubleResponse(root, "readabilityScore"))
                    .readabilityLevel(getStringResponse(root, "readabilityLevel"))
                    .tenseAnalysis(getStringResponse(root, "tenseAnalysis"))
                    // Grammar
                    .grammarIssueCount(getIntResponse(root, "grammarIssueCount"))
                    .grammarIssues(parseGrammarIssues(root))
                    // Skills
                    .matchedSkills(getStringList(root, "matchedSkills"))
                    .missingSkills(getStringList(root, "missingSkills"))
                    // Sections
                    .hasContact(getBooleanResponse(root, "hasContact"))
                    .hasExperience(getBooleanResponse(root, "hasExperience"))
                    .hasEducation(getBooleanResponse(root, "hasEducation"))
                    .hasSkills(getBooleanResponse(root, "hasSkills"))
                    .hasSummary(getBooleanResponse(root, "hasSummary"))
                    .hasProjects(getBooleanResponse(root, "hasProjects"))
                    .hasCertifications(getBooleanResponse(root, "hasCertifications"))
                    .detectedSections(getStringList(root, "detectedSections"))
                    // Contact
                    .email(getStringResponse(root, "email"))
                    .phone(getStringResponse(root, "phone"))
                    .linkedIn(getStringResponse(root, "linkedIn"))
                    .gitHub(getStringResponse(root, "gitHub"))
                    .portfolio(getStringResponse(root, "portfolio"))
                    // Verbs
                    .actionVerbs(getStringList(root, "actionVerbs"))
                    .verbFrequency(intMap(root, "verbFrequency"))
                    .overusedVerbs(getStringList(root, "overusedVerbs"))
                    .verbSuggestions(getStringList(root, "verbSuggestions"))
                    // Weak phrases
                    .weakPhrases(parseWeakPhrases(root))
                    .criticalWeakCount(getIntResponse(root, "criticalWeakCount"))
                    .minorWeakCount(getIntResponse(root, "minorWeakCount"))
                    // Achievements
                    .quantifiedAchievements(getStringList(root, "quantifiedAchievements"))
                    .achievementCount(getIntResponse(root, "achievementCount"))
                    .hasPercentages(getBooleanResponse(root, "hasPercentages"))
                    .hasMonetaryValues(getBooleanResponse(root, "hasMonetaryValues"))
                    .hasTeamSizes(getBooleanResponse(root, "hasTeamSizes"))
                    .unquantifiedBullets(getStringList(root, "unquantifiedBullets"))
                    .quantificationTips(getStringList(root, "quantificationTips"))
                    // Education
                    .highestDegree(getStringResponse(root, "highestDegree"))
                    .institution(getStringResponse(root, "institution"))
                    .graduationYear(getStringResponse(root, "graduationYear"))
                    .hasGpa(getBooleanResponse(root, "hasGpa"))
                    .hasHonours(getBooleanResponse(root, "hasHonours"))
                    // Experience
                    .dateRanges(getStringList(root, "dateRanges"))
                    .yearsExperience(getIntResponse(root, "yearsExperience"))
                    .hasCurrentRole(getBooleanResponse(root, "hasCurrentRole"))
                    .jobCount(getIntResponse(root, "jobCount"))
                    // Keywords
                    .topKeywords(getStringList(root, "topKeywords"))
                    .keywordFrequency(getMapResponse(root, "keywordFrequency"))
                    // AI Narrative
                    .executiveSummary(getStringResponse(root, "executiveSummary"))
                    .topStrength(getStringResponse(root, "topStrength"))
                    .topWeakness(getStringResponse(root, "topWeakness"))
                    .seniorityLevel(getStringResponse(root, "seniorityLevel"))
                    .inferredRole(getStringResponse(root, "inferredRole"))
                    .industry(getStringResponse(root, "industry"))
                    .aiProvider("")
                    // Feedback
                    .rewriteSuggestions(getStringList(root, "rewriteSuggestions"))
                    .redFlags(getStringList(root, "redFlags"))
                    .warnings(getStringList(root, "warnings"))
                    .suggestions(getStringList(root, "suggestions"))
                    .build();

        } catch (Exception e) {
            LOGGER.error("Failed to parse AI response: {} — returning defaults. Raw (200ch): {}",
                    e.getMessage(), rawJson.substring(0, Math.min(rawJson.length(), 200)));
            return AiAnalysisResumeResponseDTO.builder().build();
        }
    }

    private Map<String, Integer> intMap(JsonNode root, String field) {
        JsonNode n = root.get(field);
        if (n == null || !n.isObject()) return new LinkedHashMap<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        n.fields().forEachRemaining(e -> map.put(e.getKey(), e.getValue().asInt(0)));
        return map;
    }

    private List<WeakPhrase> parseWeakPhrases(JsonNode root) {
        List<WeakPhrase> list = new ArrayList<>();
        JsonNode arr = root.get("weakPhrases");
        if (arr == null) {
            return List.of();
        }
        arr.forEach(n -> {
            String sev = getStringResponse(n, "severity");
            list.add(WeakPhrase.builder()
                    .phrase(getStringResponse(n, "phrase"))
                    .severity(WeakPhrase.Severity.valueOf(sev))
                    .reason(getStringResponse(n, "reason"))
                    .betterAlternative(getStringResponse(n, "betterAlternative"))
                    .build());
        });
        return list;
    }


    private boolean getBooleanResponse(JsonNode root, String field) {
        return root.get(field).asBoolean();
    }

    private List<String> getStringList(JsonNode root, String field) {
        JsonNode n = root.get(field);
        if (n == null || !n.isArray()) return new ArrayList<>();
        List<String> list = new ArrayList<>();
        n.forEach(e -> {
            if (e.isTextual()) list.add(e.asText());
        });
        return list;
    }

    private Map<String, Integer> getMapResponse(JsonNode root, String field) {
        JsonNode n = root.get(field);
        Map<String, Integer> map = new LinkedHashMap<>();
        n.fields().forEachRemaining(e -> map.put(e.getKey(), e.getValue().asInt(0)));
        return map;

    }

    private String getStringResponse(JsonNode root, String field) {
        return root.get(field).asText();
    }

    private int getIntResponse(JsonNode root, String field) {
        return root.get(field).asInt();
    }

    private double getDoubleResponse(JsonNode root, String field) {
        return root.get(field).asDouble();
    }

    private List<GrammarIssue> parseGrammarIssues(JsonNode root) {
        JsonNode arr = root.get("grammarIssues");
        if (arr == null || !arr.isArray()) return new ArrayList<>();
        List<GrammarIssue> list = new ArrayList<>();
        arr.forEach(n -> list.add(GrammarIssue.builder()
                .errorText(getStringResponse(n, "errorText"))
                .suggestion(getStringResponse(n, "suggestion"))
                .message(getStringResponse(n, "message"))
                .category(getStringResponse(n, "category"))
                .build()));
        return list;
    }


}
