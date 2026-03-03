package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.GrammarIssue;
import com.example.jobsathi.service.GrammarCheckService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Slf4j
@Service
public class GrammarCheckServiceImpl implements GrammarCheckService {
    private  JLanguageTool languageTool ;

    @PostConstruct
    public void init() {
        LOGGER.info("Initialising GrammarCheckService");
        languageTool =new JLanguageTool(new AmericanEnglish());
        languageTool.disableRule("UPPERCASE_SENTENCE_START");
        languageTool.disableRule("EN_UNPAIRED_BRACKETS");
        languageTool.disableRule("COMMA_PARENTHESIS_WHITESPACE");
        LOGGER.info("LanguageTool initialised with {} active rules.", languageTool.getAllActiveRules().size());
    }


    @Override
    public List<GrammarIssue> checkGrammar(String text) {
        List<GrammarIssue> issues = new ArrayList<>();
        try {
            List<RuleMatch> matches = languageTool.check(text);
            for (RuleMatch match : matches) {
                String error = extractErrorText(text, match.getFromPos(), match.getToPos());

                String suggestion = match.getSuggestedReplacements().isEmpty()
                        ? "No suggestion available"
                        : match.getSuggestedReplacements().getFirst();
                issues.add(GrammarIssue.builder()
                        .message(match.getMessage())
                        .errorText(error)
                        .suggestion(suggestion)
                        .ruleId(match.getSpecificRuleId())
                        .category(match.getType().toString())
                        .fromPos(match.getFromPos())
                        .toPos(match.getToPos())
                        .build());
            }
            LOGGER.info("Grammar check complete. Found {} issues.", issues.size());
        } catch (Exception e) {
            LOGGER.error("Grammar check failed: {}", e.getMessage(), e);
        }
        return issues;
    }

    @Override
    public double score(List<GrammarIssue> issues, int wordCount) {
        if (wordCount == 0) return 0;
        double rate = (double) issues.size() / wordCount * 100;
        if (rate == 0) return 100;
        if (rate <= 0.5) return 90;
        if (rate <= 1.0) return 78;
        if (rate <= 2.0) return 60;
        if (rate <= 3.5) return 42;
        if (rate <= 5.0) return 25;
        return 10;
    }

    private String extractErrorText(String text, int from, int to) {
        return text.substring(Math.max(0, from), Math.min(text.length(), to));
    }
}
