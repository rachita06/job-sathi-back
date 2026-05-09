package com.example.jobsathi.service.util;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public class AIPromptBuilder {
    private AIPromptBuilder() {
    }

    public static String build(String resumeText, String jobDescription, String targetRole) {

        String contextBlock;

        if (jobDescription != null && !jobDescription.isBlank()) {
            contextBlock = """

            ── JOB DESCRIPTION PROVIDED ──
            JOB DESCRIPTION:
            \"\"\"
            """ + jobDescription.trim() + """
            \"\"\"

            STEP 1 — PROFESSION MISMATCH CHECK (do this before ANY scoring):
            A) Identify the profession the JD is hiring for. This can be ANY role — pilot,
               nurse, cook, driver, cleaner, security guard, electrician, software engineer,
               accountant, teacher, lawyer, waiter, gardener, mechanic, or anything else.
            B) Identify the candidate's profession from their resume titles, experience, and skills.
            C) Are they the SAME profession or closely related? (e.g. chef ↔ cook = same; pilot ↔ software engineer = different)
               - If DIFFERENT → set professionMismatch=true, resumeProfession and jdProfession to their respective detected roles
               - If SAME/RELATED → set professionMismatch=false

            STEP 2 — SCORING BASED ON MISMATCH RESULT:
            If professionMismatch=true:
               - jdMatchScore   = 5
               - keywordScore   = 5
               - matchedSkills  = []
               - missingSkills  = top 5 actual qualifications this JD requires (domain-specific, not generic)
               - DO NOT count "communication", "teamwork", "leadership", "hardworking" as matched skills
               - DO NOT inflate scores because of generic word overlap
               - skillScore     = score the resume on its OWN merits for the candidate's own profession
               - all other scores (grammar, format, experience, education, contact, verbs) = score normally

            If professionMismatch=false:
               - Score everything normally against the JD

            Instructions:
            - matchedSkills : domain-specific skills present in BOTH resume AND JD (not generic soft skills)
            - missingSkills : domain-specific skills in JD but absent from resume (max 8, most critical first)
            - keywordScore  : TF-IDF density of JD domain keywords found in resume (0–100)
            - jdMatchScore  : overall match % between resume and JD requirements (0–100)
            - inferredRole  : job title from JD if present, otherwise infer from JD content
            """;

        } else if (targetRole != null && !targetRole.isBlank()) {
            contextBlock = """

            ── TARGET ROLE PROVIDED (no full JD) ──
            The candidate is targeting: \"""" + targetRole.trim() + """
            \"

            STEP 1 — PROFESSION MISMATCH CHECK (do this before ANY scoring):
            A) Identify what profession the target role belongs to. It can be ANY role —
               cook, driver, nurse, pilot, teacher, security guard, engineer, or anything else.
            B) Identify the candidate's profession from their resume.
            C) Are they the SAME profession or closely related?
               - If DIFFERENT → set professionMismatch=true
               - If SAME/RELATED → set professionMismatch=false

            STEP 2 — SCORING BASED ON MISMATCH RESULT:
            If professionMismatch=true:
               - jdMatchScore  = 5
               - keywordScore  = 5
               - matchedSkills = []
               - missingSkills = top 5 actual qualifications this role requires
               - skillScore    = score the resume on its OWN merits for the candidate's own profession
               - all other scores = score normally

            If professionMismatch=false:
               - Infer industry-standard skills, tools, and qualifications for this role
               - Score everything normally against the target role

            Instructions:
            - matchedSkills : resume skills relevant to this target role (domain-specific only)
            - missingSkills : commonly expected skills for this role missing from resume (max 8)
            - keywordScore  : density of role-relevant domain keywords in resume (0–100)
            - jdMatchScore  : estimated fit % for this role based on resume content (0–100)
            - inferredRole  : use the provided target role as-is
            """;

        } else {
            contextBlock = """

            ── NO JD OR TARGET ROLE PROVIDED ──
            Set professionMismatch=false (nothing to mismatch against).

            Instructions:
            - Infer the candidate's role and industry purely from resume content
            - matchedSkills : strong skills present in resume relevant to the inferred role
            - missingSkills : commonly expected skills for the inferred role that are absent (max 8)
            - keywordScore  : density of role-relevant keywords in resume (0–100)
            - jdMatchScore  : null (cannot compute without a target)
            - inferredRole  : infer from job titles, skills, and experience in resume
            """;
        }

        return """
            You are an expert ATS (Applicant Tracking System) resume analyser.

            You analyse resumes from EVERY profession without exception:
            - White-collar : software engineer, doctor, lawyer, accountant, teacher, nurse, architect
            - Blue-collar  : cook, chef, driver, cleaner, security guard, electrician, plumber,
                             waiter, gardener, mechanic, carpenter, welder, factory worker, helper
            - Informal/domestic : house helper, domestic worker, caretaker, janitor, handyman
            - Any other role a human being can hold

            CRITICAL: Do NOT default to tech/software keywords for non-tech roles.
            Every profession has its own relevant skills, verbs, and keywords — use them.

            Analyse the resume and context below, then return ONLY a valid JSON object.
            No markdown, no explanation, no code fences — raw JSON only.

            Use EXACTLY this structure (every field required; null for unknown strings,
            0 for unknown numbers, [] for unknown arrays, false for unknown booleans):
            {
              "professionMismatch": false,
              "resumeProfession": "Cook",
              "jdProfession": "Cook",

              "grammarScore": 85,
              "skillScore": 72,
              "formatScore": 68,
              "keywordScore": 60,
              "experienceScore": 78,
              "educationScore": 70,
              "contactScore": 85,
              "verbDiversityScore": 56,
              "readabilityScore": 75,
              "jdMatchScore": 74,

              "readabilityLevel": "Standard",
              "tenseAnalysis": "Primarily past tense — good for experience bullets",

              "grammarIssueCount": 2,
              "grammarIssues": [
                {"errorText": "responsible for cooking", "suggestion": "Prepared", "message": "Passive phrase detected", "category": "STYLE"}
              ],

              "matchedSkills": ["Food Preparation", "Hygiene Standards", "Menu Planning"],
              "missingSkills": ["HACCP Certification", "Inventory Management", "Cost Control"],

              "hasContact": true,
              "hasExperience": true,
              "hasEducation": true,
              "hasSkills": true,
              "hasSummary": false,
              "hasProjects": false,
              "hasCertifications": false,
              "detectedSections": ["Experience", "Education", "Skills"],

              "email": "ram@example.com",
              "phone": "+977-9800000000",
              "linkedIn": null,
              "gitHub": null,
              "portfolio": null,

              "actionVerbs": ["prepared", "managed", "served", "maintained", "trained"],
              "verbFrequency": {"prepared": 3, "managed": 2, "served": 4},
              "overusedVerbs": ["served"],
              "verbSuggestions": [
                "Add variety: cooked, plated, portioned, sourced, supervised",
                "Add achievement verbs: reduced waste by X%, trained X staff"
              ],

              "weakPhrases": [
                {
                  "phrase": "helped with cooking",
                  "severity": "CRITICAL",
                  "reason": "Vague — shows no ownership",
                  "betterAlternative": "Prepared / Cooked / Produced"
                }
              ],
              "criticalWeakCount": 1,
              "minorWeakCount": 1,

              "quantifiedAchievements": [
                "Prepared meals for 200+ guests daily during peak season"
              ],
              "achievementCount": 1,
              "hasPercentages": false,
              "hasMonetaryValues": false,
              "hasTeamSizes": false,
              "unquantifiedBullets": [
                "Cooked various dishes",
                "Maintained kitchen cleanliness"
              ],
              "quantificationTips": [
                "Add metric: 'Cooked various dishes' → e.g. 'Prepared 15+ menu items daily for a 80-seat restaurant'",
                "Add metric: 'Maintained cleanliness' → e.g. 'Maintained kitchen hygiene to pass 3 health inspections with zero violations'"
              ],

              "highestDegree": "high school",
              "institution": "Kathmandu Secondary School",
              "graduationYear": "2010",
              "hasGpa": false,
              "hasHonours": false,

              "dateRanges": ["2018 - Present", "2015 - 2018"],
              "yearsExperience": 7,
              "hasCurrentRole": true,
              "jobCount": 2,

              "topKeywords": ["food preparation", "kitchen hygiene", "menu planning"],
              "keywordFrequency": {"food preparation": 4, "hygiene": 3},

              "executiveSummary": "Experienced cook with 7 years in restaurant kitchens. Strong practical skills but resume lacks quantified achievements and a professional summary.",
              "topStrength": "Solid hands-on kitchen experience with clear career progression",
              "topWeakness": "No quantified achievements and missing professional summary reduce impact",
              "seniorityLevel": "mid",
              "inferredRole": "Cook",
              "industry": "hospitality",

              "rewriteSuggestions": [
                "Replace 'helped with cooking' → 'Prepared 10+ dishes per shift maintaining quality standards'",
                "Replace 'maintained cleanliness' → 'Upheld kitchen hygiene passing all health and safety inspections'"
              ],
              "redFlags": [],
              "warnings": [
                "No professional summary — adds context to your experience",
                "Most bullet points lack numbers — add quantities to show scale of work"
              ],
              "suggestions": [
                "Add a 2-3 sentence professional summary highlighting your specialty cuisine and years of experience",
                "Quantify at least 3 bullet points — covers per day, guests served, team size",
                "Add food safety certification if you have one (e.g. HACCP, Food Handler Certificate)",
                "Add phone number if missing"
              ]
            }

            ── SCORING RULES (apply to ALL professions universally) ──

            grammarScore:
            - 100 = no errors; deduct 10 per significant grammar/spelling error
            - Apply same standard regardless of profession

            skillScore:
            - 100 = 15+ skills relevant to THEIR detected profession and/or JD
            - Scale proportionally for fewer skills
            - Use profession-appropriate skills ONLY:
              Cook/Chef        → knife skills, food prep, hygiene, menu planning, stock control, plating, kitchen equipment
              Driver           → valid licence class, defensive driving, route planning, vehicle maintenance, cargo handling, GPS
              Cleaner/Helper   → cleaning techniques, chemical safety, time management, equipment operation, attention to detail
              Security Guard   → surveillance, access control, emergency response, report writing, first aid
              Electrician      → wiring, circuit testing, safety standards, blueprints, fault diagnosis
              Nurse            → patient assessment, medication admin, wound care, clinical documentation, IV therapy
              Teacher          → lesson planning, classroom management, curriculum development, assessment, student engagement
              Software Engineer→ programming languages, frameworks, databases, version control, system design
              Accountant       → bookkeeping, financial reporting, tax preparation, auditing, reconciliation
              (use equivalent domain-specific skills for any other profession detected)

            formatScore:
            - Sections present (40pts) + word count 300–700 (30pts) + bullets used (20pts) + contact complete (10pts)
            - Note: blue-collar resumes are often shorter (150–400 words) — do not penalise heavily for brevity

            keywordScore:
            - JD provided → TF-IDF density of JD's domain-specific keywords found in resume (0–100)
            - Target role → density of role-relevant domain keywords (0–100)
            - No JD/role → density of keywords relevant to inferred role (0–100)
            - MUST be ≤ 10 if professionMismatch=true

            experienceScore:
            - 0–1yr → 20, 2yr → 40, 3–4yr → 65, 5–6yr → 78, 7–8yr → 88, 9+yr → 100
            - Score based on resume content, NOT JD fit

            educationScore:
            - No formal education → 10, Primary school → 20, High school → 35, Vocational/trade cert → 50,
              Diploma → 60, Bachelor → 75, Master → 88, PhD → 100
            - For blue-collar roles: vocational/trade certification is highly relevant — weight it appropriately

            contactScore:
            - email → 35pts, phone → 35pts, linkedin → 20pts, other (portfolio/github/social) → 10pts
            - Note: blue-collar workers often have no LinkedIn — do not penalise heavily; phone is more important for them

            verbDiversityScore:
            - 7 profession-appropriate verb categories × 14pts each
            - Use the correct categories for the detected profession:
              Hospitality/Food → preparation / service / hygiene / coordination / stock management / customer interaction / achievement
              Transport/Driver → operation / navigation / maintenance / safety / coordination / communication / achievement
              Domestic/Helper  → cleaning / organising / caregiving / cooking / maintenance / communication / achievement
              Construction     → installation / maintenance / operation / safety / coordination / physical execution / achievement
              Healthcare       → patient care / clinical / documentation / leadership / communication / research / achievement
              Education        → planning / instruction / assessment / mentoring / communication / administration / achievement
              Tech             → building / analysis / delivery / leadership / optimisation / communication / achievement
              Finance          → analysis / reporting / advisory / compliance / leadership / communication / achievement
              (adapt to whatever profession is detected)

            jdMatchScore:
            - professionMismatch=true  → 5 (non-negotiable)
            - JD provided, no mismatch → overlap % of JD requirements vs resume (0–100)
            - Target role, no mismatch → estimated fit % (0–100)
            - No JD or role            → null

            ── PROFESSION GUIDELINES — NON-NEGOTIABLE ──
            - Detect profession from resume FIRST; override with JD/targetRole if provided
            - NEVER apply tech keywords to non-tech roles
            - NEVER penalise a cook for missing Docker, a driver for missing Python, a nurse for missing React
            - missingSkills MUST be relevant to their actual profession and the JD/targetRole
            - inferredRole must be specific and honest:
              "Cook", "Head Chef", "Delivery Driver", "House Cleaner", "Security Guard",
              "Registered Nurse", "Primary School Teacher", "Software Engineer", "Financial Analyst"
            - resumeProfession: what the candidate actually IS based on their resume
            - jdProfession: what the JD/targetRole is actually looking for
            - seniorityLevel: "entry" | "junior" | "mid" | "senior" | "lead" | "executive"
              (for blue-collar: entry=0-1yr, junior=1-3yr, mid=3-6yr, senior=6+yr, lead=supervises others)
            - industry: single lowercase word — "hospitality", "transport", "domestic", "construction",
              "healthcare", "education", "legal", "finance", "marketing", "software", "security",
              "manufacturing", "retail", "agriculture", or whatever fits best
            """ + contextBlock + """

            ── RESUME TO ANALYSE ──
            \"\"\"
            """ + resumeText.trim() + """
            \"\"\"
            """;
    }
}