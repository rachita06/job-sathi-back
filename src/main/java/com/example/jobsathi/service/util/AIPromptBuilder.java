package com.example.jobsathi.service.util;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public class AIPromptBuilder {
    private AIPromptBuilder() {
    }

    private static final int MAX_CHARS = 6000;

    public static String build(String resumeText) {
        String excerpt = resumeText.length() > MAX_CHARS
                ? resumeText.substring(0, MAX_CHARS) : resumeText;

        return """
                You are an expert ATS (Applicant Tracking System) resume analyser.
                Analyse the resume below and return ONLY a valid JSON object.
                No markdown, no explanation, no code fences — just raw JSON.

                Use EXACTLY this structure (every field required, use null for unknown strings, 0 for unknown numbers, [] for unknown arrays):
                {
                  "grammarScore": 85,
                  "skillScore": 72,
                  "formatScore": 68,
                  "keywordScore": 60,
                  "experienceScore": 78,
                  "educationScore": 70,
                  "contactScore": 85,
                  "verbDiversityScore": 56,
                  "readabilityScore": 75,
                  "readabilityLevel": "Standard",
                  "tenseAnalysis": "Primarily past tense — good for experience bullets",
                  "grammarIssueCount": 2,
                  "grammarIssues": [
                    {"errorText": "responsible for", "suggestion": "Led", "message": "Passive phrase detected", "category": "STYLE"}
                  ],
                  "matchedSkills": ["Java", "Spring Boot", "Docker", "PostgreSQL"],
                  "missingSkills": ["Kubernetes", "AWS", "CI/CD", "Redis"],
                  "hasContact": true,
                  "hasExperience": true,
                  "hasEducation": true,
                  "hasSkills": true,
                  "hasSummary": false,
                  "hasProjects": true,
                  "hasCertifications": false,
                  "detectedSections": ["Experience", "Education", "Skills", "Projects"],
                  "email": "john@example.com",
                  "phone": "+1-555-0100",
                  "linkedIn": "linkedin.com/in/johndoe",
                  "gitHub": "github.com/johndoe",
                  "portfolio": null,
                  "actionVerbs": ["led", "built", "developed", "deployed", "optimised"],
                  "verbFrequency": {"led": 2, "built": 3, "developed": 4},
                  "overusedVerbs": ["developed"],
                  "verbSuggestions": [
                    "Add achievement verbs: exceeded, grew, generated, secured",
                    "Add leadership verbs: mentored, directed, championed"
                  ],
                  "weakPhrases": [
                    {
                      "phrase": "responsible for",
                      "severity": "CRITICAL",
                      "reason": "Passive ownership — shows no initiative",
                      "betterAlternative": "Led / Owned / Drove / Built"
                    }
                  ],
                  "criticalWeakCount": 1,
                  "minorWeakCount": 2,
                  "quantifiedAchievements": [
                    "Reduced API latency by 40% through query optimisation",
                    "Led a team of 6 engineers to deliver product on time"
                  ],
                  "achievementCount": 2,
                  "hasPercentages": true,
                  "hasMonetaryValues": false,
                  "hasTeamSizes": true,
                  "unquantifiedBullets": [
                    "Worked on backend microservices",
                    "Helped with deployment pipeline"
                  ],
                  "quantificationTips": [
                    "Add metric to: Worked on backend microservices → e.g. Built 3 microservices handling 100K requests/day",
                    "Add metric to: Helped with deployment pipeline → e.g. Reduced deployment time by 30%"
                  ],
                  "highestDegree": "bachelor",
                  "institution": "University of London",
                  "graduationYear": "2019",
                  "hasGpa": false,
                  "hasHonours": false,
                  "dateRanges": ["2021 - Present", "2019 - 2021"],
                  "yearsExperience": 4,
                  "hasCurrentRole": true,
                  "jobCount": 2,
                  "topKeywords": ["Java", "Spring Boot", "microservices", "REST API", "Docker"],
                  "keywordFrequency": {"java": 6, "spring": 4, "docker": 2},
                  "executiveSummary": "Solid backend engineer with 4 years Java/Spring experience. Good technical depth but missing quantified achievements and professional summary.",
                  "topStrength": "Strong technical skill set with relevant keywords for ATS",
                  "topWeakness": "No professional summary and several unquantified bullets reduce impact",
                  "seniorityLevel": "mid",
                  "inferredRole": "Backend Java Developer",
                  "industry": "software",
                  "rewriteSuggestions": [
                    "Replace 'responsible for APIs' → 'Architected 5 RESTful APIs serving 200K daily users'",
                    "Replace 'worked on deployment' → 'Automated CI/CD pipeline reducing release time by 35%'"
                  ],
                  "redFlags": [],
                  "warnings": [
                    "No professional summary — ATS systems rank summaries highly",
                    "Only 4 verb categories used — diversify to show broader impact"
                  ],
                  "suggestions": [
                    "Add a 2-3 sentence professional summary at the top",
                    "Quantify at least 3 more bullet points with numbers or percentages",
                    "Add missing high-value skills: Kubernetes, AWS, Terraform",
                    "Add LinkedIn URL to improve contact completeness"
                  ]
                }

                Scoring rules (strictly follow these):
                - grammarScore   : 100=no errors; −10 per significant grammar/spelling error
                - skillScore     : 100=15+ relevant tech skills; scale proportionally
                - formatScore    : sections present(40pts) + word count 300-800(30pts) + bullet points used(20pts) + contact complete(10pts)
                - keywordScore   : ATS keyword density — 30 tech keywords × TF score
                - experienceScore: 0-1yr→20, 2yr→40, 3-4yr→65, 5-6yr→78, 7-8yr→88, 9+yr→100
                - educationScore : high school→20, diploma→40, bachelor→70, master→85, phd→100
                - contactScore   : email→35pts, phone→25pts, linkedin→25pts, github→15pts
                - verbDiversityScore: 7 verb categories × 14pts each (leadership/building/analysis/delivery/communication/optimisation/achievement)

                RESUME TO ANALYSE:
                """ + excerpt;
    }
}
