package com.example.jobsathi.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Data
@Builder
public class GrammarIssue {
    private String message;
    private String errorText;
    private String suggestion;
    private String ruleId;
    private String category;
    private int fromPos;
    private int toPos;
}
