package com.example.jobsathi.service;

import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.dto.response.ResumeScoreResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Rabindra Adhikari on 3/1/26
 */

@Service
@Slf4j
public class TFIDFAlgorithmImpl implements TFIDFAlgorithm {

    private static final double SKILL_WEIGHT = 0.28;
    private static final double GRAMMAR_WEIGHT = 0.18;
    private static final double FORMAT_WEIGHT = 0.18;
    private static final double KEYWORD_WEIGHT = 0.18;
    private static final double EXPERIENCE_WEIGHT = 0.14;
    private static final double EDUCATION_WEIGHT = 0.14;
    private static final double CONTACT_WEIGHT = 0.14;
    private static final double VERBS_WEIGHT = 0.14;


    @Override
    public ResumeScoreResponseDTO useTFIDFAlgorithm(AiAnalysisResumeResponseDTO ai,String fileType,
                                                    String rawText) {
        double overall = getOverall(ai);

        String formula = String.format(
                "Skills(%.0f)x28%% + Grammar(%.0f)x18%% + Format(%.0f)x18%% + " +
                        "Keywords(%.0f)x14%% + Experience(%.0f)x12%% + Education(%.0f)x5%% + " +
                        "Contact(%.0f)x3%% + VerbDiv(%.0f)x2%%",
                ai.getSkillScore(), ai.getGrammarScore(), ai.getFormatScore(),
                ai.getKeywordScore(), ai.getExperienceScore(), ai.getEducationScore(),
                ai.getContactScore(), ai.getVerbDiversityScore());

        String grade = grade(overall);
        String verdict = verdict(overall);
        String feedback = buildFeedback(overall, ai);
        String preview = rawText.length() > 300
                ? rawText.substring(0, 300).replaceAll("\\s+", " ") + "..."
                : rawText;
        int sentences = Math.max(1, rawText.split("[.!?]+").length);

        LOGGER.info("Final score: {}/100  Grade: {}  Provider: {}",
                overall, grade, ai.getAiProvider());

        return ResumeScoreResponseDTO.builder()
                .overallScore(overall).grade(grade).verdict(verdict)
                .feedback(feedback).scoringFormula(formula)
                .skillScore(r1(ai.getSkillScore())).grammarScore(r1(ai.getGrammarScore()))
                .formatScore(r1(ai.getFormatScore())).keywordScore(r1(ai.getKeywordScore()))
                .experienceScore(r1(ai.getExperienceScore())).educationScore(r1(ai.getEducationScore()))
                .contactScore(r1(ai.getContactScore())).verbDiversityScore(r1(ai.getVerbDiversityScore()))
                .hasContact(ai.isHasContact()).hasExperience(ai.isHasExperience())
                .hasEducation(ai.isHasEducation()).hasSkills(ai.isHasSkills())
                .hasSummary(ai.isHasSummary()).hasProjects(ai.isHasProjects())
                .hasCertifications(ai.isHasCertifications()).detectedSections(ai.getDetectedSections())
                .email(ai.getEmail()).phone(ai.getPhone())
                .linkedIn(ai.getLinkedIn()).gitHub(ai.getGitHub()).portfolio(ai.getPortfolio())
                .matchedSkills(ai.getMatchedSkills()).missingSkills(ai.getMissingSkills())
                .grammarIssueCount(ai.getGrammarIssueCount()).grammarIssues(ai.getGrammarIssues())
                .actionVerbsFound(ai.getActionVerbs()).verbFrequency(ai.getVerbFrequency())
                .overusedVerbs(ai.getOverusedVerbs()).verbSuggestions(ai.getVerbSuggestions())
                .weakPhrases(ai.getWeakPhrases())
                .criticalWeakCount(ai.getCriticalWeakCount()).minorWeakCount(ai.getMinorWeakCount())
                .quantifiedAchievements(ai.getQuantifiedAchievements())
                .achievementCount(ai.getAchievementCount())
                .hasPercentages(ai.isHasPercentages()).hasMonetaryValues(ai.isHasMonetaryValues())
                .hasTeamSizes(ai.isHasTeamSizes())
                .unquantifiedBullets(ai.getUnquantifiedBullets())
                .quantificationTips(ai.getQuantificationTips())
                .highestDegree(ai.getHighestDegree()).institution(ai.getInstitution())
                .graduationYear(ai.getGraduationYear())
                .hasGpa(ai.isHasGpa()).hasHonours(ai.isHasHonours())
                .dateRanges(ai.getDateRanges()).yearsExperience(ai.getYearsExperience())
                .hasCurrentRole(ai.isHasCurrentRole()).jobCount(ai.getJobCount())
                .readabilityScore(ai.getReadabilityScore()).readabilityLevel(ai.getReadabilityLevel())
                .tenseAnalysis(ai.getTenseAnalysis())
                .topKeywords(ai.getTopKeywords()).keywordFrequency(ai.getKeywordFrequency())
                .executiveSummary(ai.getExecutiveSummary())
                .topStrength(ai.getTopStrength()).topWeakness(ai.getTopWeakness())
                .seniorityLevel(ai.getSeniorityLevel()).inferredRole(ai.getInferredRole())
                .industry(ai.getIndustry()).aiProvider(ai.getAiProvider())
                .rewriteSuggestions(ai.getRewriteSuggestions())
                .redFlags(ai.getRedFlags()).warnings(ai.getWarnings()).suggestions(ai.getSuggestions())
                .wordCount(rawText.length()).sentenceCount(sentences)
                .fileType(fileType).extractedTextPreview(preview)
                .build();
    }

    private double getOverall(AiAnalysisResumeResponseDTO ai) {
        double overall = r1(
                (ai.getSkillScore() * SKILL_WEIGHT)
                        + (ai.getGrammarScore() * GRAMMAR_WEIGHT)
                        + (ai.getFormatScore() * FORMAT_WEIGHT)
                        + (ai.getKeywordScore() * KEYWORD_WEIGHT)
                        + (ai.getExperienceScore() * EXPERIENCE_WEIGHT)
                        + (ai.getEducationScore() * EDUCATION_WEIGHT)
                        + (ai.getContactScore() * CONTACT_WEIGHT)
                        + (ai.getVerbDiversityScore() * VERBS_WEIGHT)
        );

        overall = Math.max(0, Math.min(100, overall));
        return overall;
    }

    public String grade(double s) {
        if (s >= 85) return "A";
        if (s >= 70) return "B";
        if (s >= 55) return "C";
        if (s >= 40) return "D";
        return "F";
    }

    public String verdict(double s) {
        if (s >= 85) return "Strong Resume — Likely to pass ATS screening";
        if (s >= 70) return "Good Resume — Minor improvements recommended";
        if (s >= 55) return "Average Resume — Moderate revision required";
        if (s >= 40) return "Weak Resume — Significant revision required";
        return "Poor Resume — Major overhaul recommended";
    }

    private String buildFeedback(double score, AiAnalysisResumeResponseDTO ai) {
        StringBuilder sb = new StringBuilder();
        if (score >= 85) sb.append("Strong resume. ");
        else if (score >= 70) sb.append("Good resume with room to improve. ");
        else if (score >= 55) sb.append("Average resume — needs work. ");
        else sb.append("Resume needs significant revision. ");

        int skills = ai.getMatchedSkills() != null ? ai.getMatchedSkills().size() : 0;
        sb.append(skills).append(" skills detected. ");
        if (ai.getGrammarIssueCount() > 0) sb.append(ai.getGrammarIssueCount()).append(" grammar issue(s). ");
        if (ai.getCriticalWeakCount() > 0)
            sb.append(ai.getCriticalWeakCount()).append(" passive phrase(s) to replace. ");
        if (ai.getAchievementCount() == 0) sb.append("No quantified achievements — add metrics. ");
        if (!ai.isHasSummary()) sb.append("Missing professional summary. ");
        return sb.toString().trim();
    }

    private double r1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
