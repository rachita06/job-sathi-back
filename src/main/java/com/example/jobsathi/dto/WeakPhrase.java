package com.example.jobsathi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeakPhrase {
    public enum Severity { CRITICAL, MINOR }
    private String phrase;
    private Severity severity;
    private String reason;
    private String betterAlternative;
}