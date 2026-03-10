package com.example.jobsathi.dto.response;

import com.example.jobsathi.dto.GrammarIssue;
import com.example.jobsathi.dto.WeakPhrase;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Complete resume score response returned to the API caller.
 * Every field is populated — nothing is null if the pipeline ran successfully.
 */
@Data
@Builder
public class ResumeScoreResponseDTO {

    // ── Overall Result ────────────────────────────────────────────────────────
    private double overallScore;
    private String grade;
    private String verdict;
    private String feedback;

    // ── Algorithm Breakdown ───────────────────────────────────────────────────
    private double tfidfScore;
    private Double semanticScore;
    private double patternScore;
    private String scoringFormula;

    // ── TF-IDF Dimension Scores (0–100 each) ──────────────────────────────────
    private double skillScore;
    private double grammarScore;
    private double formatScore;
    private double keywordScore;
    private double experienceScore;
    private double educationScore;
    private double contactScore;
    private double verbDiversityScore;

    // ── Sections Detected ─────────────────────────────────────────────────────
    private boolean hasContact;
    private boolean hasExperience;
    private boolean hasEducation;
    private boolean hasSkills;
    private boolean hasSummary;
    private boolean hasProjects;
    private boolean hasCertifications;
    private List<String> detectedSections;

    // ── Contact Info Extracted ────────────────────────────────────────────────
    private String email;
    private String phone;
    private String linkedIn;
    private String gitHub;
    private String portfolio;

    // ── Skills ────────────────────────────────────────────────────────────────
    private List<String> matchedSkills;
    private List<String> missingSkills;

    // ── Grammar ───────────────────────────────────────────────────────────────
    private int grammarIssueCount;
    private List<GrammarIssue> grammarIssues;

    // ── Action Verbs ──────────────────────────────────────────────────────────
    private List<String> actionVerbsFound;
    private Map<String, Integer> verbFrequency;
    private List<String> overusedVerbs;
    private List<String> verbSuggestions;

    // ── Weak Phrases ──────────────────────────────────────────────────────────
    private List<WeakPhrase> weakPhrases;
    private int criticalWeakCount;
    private int minorWeakCount;

    // ── Quantified Achievements ───────────────────────────────────────────────
    private List<String> quantifiedAchievements;
    private int achievementCount;
    private boolean hasPercentages;
    private boolean hasMonetaryValues;
    private boolean hasTeamSizes;
    private List<String> unquantifiedBullets;
    private List<String> quantificationTips;

    // ── Education ─────────────────────────────────────────────────────────────
    private String highestDegree;
    private String institution;
    private String graduationYear;
    private boolean hasGpa;
    private boolean hasHonours;

    // ── Experience ────────────────────────────────────────────────────────────
    private List<String> dateRanges;
    private int yearsExperience;
    private boolean hasCurrentRole;
    private int jobCount;

    // ── Readability ───────────────────────────────────────────────────────────
    private double readabilityScore;
    private String readabilityLevel;
    private String toneAssessment;
    private String tenseAnalysis;
    private double avgSentenceLength;

    // ── Keywords (TF Analysis) ────────────────────────────────────────────────
    private Map<String, Integer> keywordFrequency;
    private Map<String, Double> keywordTfScores;
    private List<String> topKeywords;
    private List<String> missingKeywords;

    // ── AI Insights (when AI provider is configured) ──────────────────────────
    private String executiveSummary;
    private String topStrength;
    private String topWeakness;
    private String inferredRole;
    private String seniorityLevel;
    private String industry;
    private List<String> rewriteSuggestions;
    private String aiProvider;

    private List<String> redFlags;
    private List<String> warnings;
    private List<String> suggestions;

    // ── Document Statistics ───────────────────────────────────────────────────
    private int wordCount;
    private int sentenceCount;
    private int bulletCount;
    private String fileType;
    private String extractedTextPreview;
}