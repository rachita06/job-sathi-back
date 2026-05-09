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

    // ── Weights MUST sum to exactly 1.0 ──────────────────────────────────────
    // Previous weights summed to 1.38 — causing inflated scores near 100 for avg resumes
    private static final double SKILL_WEIGHT       = 0.25;  // 25% — strongest signal
    private static final double GRAMMAR_WEIGHT     = 0.15;  // 15%
    private static final double FORMAT_WEIGHT      = 0.15;  // 15%
    private static final double KEYWORD_WEIGHT     = 0.15;  // 15%
    private static final double EXPERIENCE_WEIGHT  = 0.12;  // 12%
    private static final double EDUCATION_WEIGHT   = 0.08;  //  8%
    private static final double CONTACT_WEIGHT     = 0.06;  //  6%
    private static final double VERBS_WEIGHT       = 0.04;  //  4%
    // Total: 0.25 + 0.15 + 0.15 + 0.15 + 0.12 + 0.08 + 0.06 + 0.04 = 1.00 ✅

    @Override
    public ResumeScoreResponseDTO useTFIDFAlgorithm(AiAnalysisResumeResponseDTO ai,
                                                    String fileType,
                                                    String rawText) {
        double overall = getOverall(ai);

        String formula = String.format(
                "Skills(%.0f)×25%% + Grammar(%.0f)×15%% + Format(%.0f)×15%% + " +
                        "Keywords(%.0f)×15%% + Exp(%.0f)×12%% + Edu(%.0f)×8%% + " +
                        "Contact(%.0f)×6%% + Verbs(%.0f)×4%%",
                ai.getSkillScore(), ai.getGrammarScore(), ai.getFormatScore(),
                ai.getKeywordScore(), ai.getExperienceScore(), ai.getEducationScore(),
                ai.getContactScore(), ai.getVerbDiversityScore());

        String grade    = grade(overall);
        String verdict  = verdict(overall);
        String feedback = buildFeedback(overall, ai);

        String preview = rawText.length() > 300
                ? rawText.substring(0, 300).replaceAll("\\s+", " ") + "..."
                : rawText;

        int sentences = Math.max(1, rawText.split("[.!?]+").length);

        LOGGER.info("Final score: {}/100  Grade: {}  Provider: {}",
                overall, grade, ai.getAiProvider());

        return ResumeScoreResponseDTO.builder()
                // ── Core scores ────────────────────────────────────────────
                .overallScore(overall)
                .grade(grade)
                .verdict(verdict)
                .feedback(feedback)
                .scoringFormula(formula)
                .skillScore(r1(ai.getSkillScore()))
                .grammarScore(r1(ai.getGrammarScore()))
                .formatScore(r1(ai.getFormatScore()))
                .keywordScore(r1(ai.getKeywordScore()))
                .experienceScore(r1(ai.getExperienceScore()))
                .educationScore(r1(ai.getEducationScore()))
                .contactScore(r1(ai.getContactScore()))
                .verbDiversityScore(r1(ai.getVerbDiversityScore()))
                .jdMatchScore(ai.getJdMatchScore())
                // ── Section flags ──────────────────────────────────────────
                .hasContact(ai.isHasContact())
                .hasExperience(ai.isHasExperience())
                .hasEducation(ai.isHasEducation())
                .hasSkills(ai.isHasSkills())
                .hasSummary(ai.isHasSummary())
                .hasProjects(ai.isHasProjects())
                .hasCertifications(ai.isHasCertifications())
                .detectedSections(ai.getDetectedSections())
                // ── Contact ────────────────────────────────────────────────
                .email(ai.getEmail())
                .phone(ai.getPhone())
                .linkedIn(ai.getLinkedIn())
                .gitHub(ai.getGitHub())
                .portfolio(ai.getPortfolio())
                // ── Skills ─────────────────────────────────────────────────
                .matchedSkills(ai.getMatchedSkills())
                .missingSkills(ai.getMissingSkills())
                // ── Grammar ────────────────────────────────────────────────
                .grammarIssueCount(ai.getGrammarIssueCount())
                .grammarIssues(ai.getGrammarIssues())
                // ── Verbs ──────────────────────────────────────────────────
                .actionVerbsFound(ai.getActionVerbs())
                .verbFrequency(ai.getVerbFrequency())
                .overusedVerbs(ai.getOverusedVerbs())
                .verbSuggestions(ai.getVerbSuggestions())
                // ── Weak phrases ───────────────────────────────────────────
                .weakPhrases(ai.getWeakPhrases())
                .criticalWeakCount(ai.getCriticalWeakCount())
                .minorWeakCount(ai.getMinorWeakCount())
                // ── Achievements ───────────────────────────────────────────
                .quantifiedAchievements(ai.getQuantifiedAchievements())
                .achievementCount(ai.getAchievementCount())
                .hasPercentages(ai.isHasPercentages())
                .hasMonetaryValues(ai.isHasMonetaryValues())
                .hasTeamSizes(ai.isHasTeamSizes())
                .unquantifiedBullets(ai.getUnquantifiedBullets())
                .quantificationTips(ai.getQuantificationTips())
                // ── Education ──────────────────────────────────────────────
                .highestDegree(ai.getHighestDegree())
                .institution(ai.getInstitution())
                .graduationYear(ai.getGraduationYear())
                .hasGpa(ai.isHasGpa())
                .hasHonours(ai.isHasHonours())
                // ── Experience ─────────────────────────────────────────────
                .dateRanges(ai.getDateRanges())
                .yearsExperience(ai.getYearsExperience())
                .hasCurrentRole(ai.isHasCurrentRole())
                .jobCount(ai.getJobCount())
                // ── Readability ────────────────────────────────────────────
                .readabilityScore(ai.getReadabilityScore())
                .readabilityLevel(ai.getReadabilityLevel())
                .tenseAnalysis(ai.getTenseAnalysis())
                // ── Keywords ───────────────────────────────────────────────
                .topKeywords(ai.getTopKeywords())
                .keywordFrequency(ai.getKeywordFrequency())
                // ── AI insights ────────────────────────────────────────────
                .executiveSummary(ai.getExecutiveSummary())
                .topStrength(ai.getTopStrength())
                .topWeakness(ai.getTopWeakness())
                .seniorityLevel(ai.getSeniorityLevel())
                .inferredRole(ai.getInferredRole())
                .industry(ai.getIndustry())
                .aiProvider(ai.getAiProvider())
                .rewriteSuggestions(ai.getRewriteSuggestions())
                .redFlags(ai.getRedFlags())
                .warnings(ai.getWarnings())
                .suggestions(ai.getSuggestions())
                // ── Document meta ──────────────────────────────────────────
                .wordCount(rawText.split("\\s+").length)   // word count, not char count
                .sentenceCount(sentences)
                .fileType(fileType)
                .extractedTextPreview(preview)
                .tfidfScore(overall)
                .build();
    }

    // ── Overall score calculation ─────────────────────────────────────────────
    private double getOverall(AiAnalysisResumeResponseDTO ai) {

        // Step 1: weighted sum (weights sum to 1.0, so raw is already 0–100)
        double raw =
                (ai.getSkillScore()        * SKILL_WEIGHT)
                        + (ai.getGrammarScore()      * GRAMMAR_WEIGHT)
                        + (ai.getFormatScore()       * FORMAT_WEIGHT)
                        + (ai.getKeywordScore()      * KEYWORD_WEIGHT)
                        + (ai.getExperienceScore()   * EXPERIENCE_WEIGHT)
                        + (ai.getEducationScore()    * EDUCATION_WEIGHT)
                        + (ai.getContactScore()      * CONTACT_WEIGHT)
                        + (ai.getVerbDiversityScore()* VERBS_WEIGHT);

        // Step 2: section-missing penalties
        // These prevent structurally incomplete resumes from scoring high
        double penalty = 0;

        if (!ai.isHasContact())            penalty += 4.0;  // no contact info at all
        if (!ai.isHasExperience())         penalty += 5.0;  // no experience section
        if (!ai.isHasSkills())             penalty += 3.0;  // no skills section
        if (!ai.isHasSummary())            penalty += 2.0;  // missing professional summary
        if (ai.getAchievementCount() == 0) penalty += 3.0;  // zero quantified achievements
        if (ai.getGrammarIssueCount() > 5) penalty += 2.0;  // excessive grammar issues
        if (ai.getCriticalWeakCount() > 3) penalty += 2.0;  // too many passive/weak phrases
        if (ai.getMissingSkills() != null
                && ai.getMissingSkills().size() > 6) penalty += 1.5; // many missing skills

        double overall = raw - penalty;

        // Step 3: clamp to [0, 100] — no artificial ceiling at 90
        return r1(Math.max(0, Math.min(100, overall)));
    }

    // ── Grade ─────────────────────────────────────────────────────────────────
    public String grade(double s) {
        if (s >= 88) return "A+";
        if (s >= 78) return "A";
        if (s >= 68) return "B";
        if (s >= 55) return "C";
        if (s >= 40) return "D";
        return "F";
    }

    // ── Verdict ───────────────────────────────────────────────────────────────
    public String verdict(double s) {
        if (s >= 88) return "Excellent Resume — Very likely to pass ATS screening";
        if (s >= 78) return "Strong Resume — Good chance of passing ATS screening";
        if (s >= 68) return "Good Resume — Minor improvements recommended";
        if (s >= 55) return "Average Resume — Moderate revision required";
        if (s >= 40) return "Weak Resume — Significant revision required";
        return "Poor Resume — Major overhaul recommended";
    }

    // ── Feedback ──────────────────────────────────────────────────────────────
    private String buildFeedback(double score, AiAnalysisResumeResponseDTO ai) {
        StringBuilder sb = new StringBuilder();

        // Opening line based on score
        if (score >= 88)      sb.append("Excellent resume. ");
        else if (score >= 78) sb.append("Strong resume with a few areas to polish. ");
        else if (score >= 68) sb.append("Good resume with room to improve. ");
        else if (score >= 55) sb.append("Average resume — needs targeted improvements. ");
        else                  sb.append("Resume needs significant revision. ");

        // Skill count
        int skills = ai.getMatchedSkills() != null ? ai.getMatchedSkills().size() : 0;
        if (skills > 0) sb.append(skills).append(" relevant skill(s) detected. ");

        // Grammar
        if (ai.getGrammarIssueCount() > 0)
            sb.append(ai.getGrammarIssueCount()).append(" grammar issue(s) found. ");

        // Passive phrases
        if (ai.getCriticalWeakCount() > 0)
            sb.append(ai.getCriticalWeakCount()).append(" passive phrase(s) to replace. ");

        // Achievements
        if (ai.getAchievementCount() == 0)
            sb.append("No quantified achievements — add metrics for impact. ");
        else
            sb.append(ai.getAchievementCount()).append(" quantified achievement(s) found. ");

        // Missing sections
        if (!ai.isHasSummary())    sb.append("Missing professional summary. ");
        if (!ai.isHasContact())    sb.append("Contact info incomplete. ");
        if (!ai.isHasSkills())     sb.append("Skills section not detected. ");
        if (!ai.isHasExperience()) sb.append("Experience section not detected. ");

        // JD match hint
        if (ai.getJdMatchScore() != null)
            sb.append(String.format("JD match score: %.0f%%. ", ai.getJdMatchScore()));

        return sb.toString().trim();
    }

    // ── Utility ───────────────────────────────────────────────────────────────
    private double r1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}