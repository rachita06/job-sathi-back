package com.example.jobsathi.dto.response;

import com.example.jobsathi.dto.GrammarIssue;
import com.example.jobsathi.dto.WeakPhrase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResumeResponseDTO {

    // ── Dimension scores 0-100 — fed into ScoringAlgorithm ───────────────────
    private double grammarScore;
    private double skillScore;
    private double formatScore;
    private double keywordScore;
    private double experienceScore;
    private double educationScore;
    private double contactScore;
    private double verbDiversityScore;

    // ── Grammar ───────────────────────────────────────────────────────────────
    private int                grammarIssueCount;
    private List<GrammarIssue> grammarIssues;

    // ── Skills ────────────────────────────────────────────────────────────────
    private List<String> matchedSkills;
    private List<String> missingSkills;

    // ── Sections ──────────────────────────────────────────────────────────────
    private boolean      hasContact;
    private boolean      hasExperience;
    private boolean      hasEducation;
    private boolean      hasSkills;
    private boolean      hasSummary;
    private boolean      hasProjects;
    private boolean      hasCertifications;
    private List<String> detectedSections;

    // ── Contact ───────────────────────────────────────────────────────────────
    private String email;
    private String phone;
    private String linkedIn;
    private String gitHub;
    private String portfolio;

    // ── Action Verbs ──────────────────────────────────────────────────────────
    private List<String>         actionVerbs;
    private Map<String, Integer> verbFrequency;
    private List<String>         overusedVerbs;
    private List<String>         verbSuggestions;

    // ── Weak Phrases ──────────────────────────────────────────────────────────
    private List<WeakPhrase> weakPhrases;
    private int              criticalWeakCount;
    private int              minorWeakCount;

    // ── Achievements ──────────────────────────────────────────────────────────
    private List<String> quantifiedAchievements;
    private int          achievementCount;
    private boolean      hasPercentages;
    private boolean      hasMonetaryValues;
    private boolean      hasTeamSizes;
    private List<String> unquantifiedBullets;
    private List<String> quantificationTips;

    // ── Education ─────────────────────────────────────────────────────────────
    private String  highestDegree;
    private String  institution;
    private String  graduationYear;
    private boolean hasGpa;
    private boolean hasHonours;

    // ── Experience ────────────────────────────────────────────────────────────
    private List<String> dateRanges;
    private int          yearsExperience;
    private boolean      hasCurrentRole;
    private int          jobCount;

    // ── Readability ───────────────────────────────────────────────────────────
    private double readabilityScore;
    private String readabilityLevel;
    private String tenseAnalysis;

    // ── Keywords ──────────────────────────────────────────────────────────────
    private List<String>         topKeywords;
    private Map<String, Integer> keywordFrequency;

    // ── AI Narrative ──────────────────────────────────────────────────────────
    private String executiveSummary;
    private String topStrength;
    private String topWeakness;
    private String seniorityLevel;
    private String inferredRole;
    private String industry;
    private String aiProvider;

    // ── Feedback ──────────────────────────────────────────────────────────────
    private List<String> rewriteSuggestions;
    private List<String> redFlags;
    private List<String> warnings;
    private List<String> suggestions;
}

