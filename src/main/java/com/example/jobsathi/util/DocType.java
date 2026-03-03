package com.example.jobsathi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@AllArgsConstructor
@Getter
public enum DocType {
    PDF("pdf"),
    DOCX("docx");
    private final String desc;

    public static DocType fromExtension(String extension) {
        return Arrays.stream(values())
                .filter(type -> type.getDesc().equalsIgnoreCase(extension))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unsupported extension: " + extension)
                );
    }

}
