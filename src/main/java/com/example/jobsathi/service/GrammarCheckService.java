package com.example.jobsathi.service;

import com.example.jobsathi.dto.GrammarIssue;

import java.util.List;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
public interface GrammarCheckService {
    List<GrammarIssue> checkGrammar(String text);
    double score (List<GrammarIssue> issues,int wordCount);
}
