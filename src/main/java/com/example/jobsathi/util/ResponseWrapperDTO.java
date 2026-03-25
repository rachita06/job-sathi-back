package com.example.jobsathi.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Rabindra Adhikari on 3/25/26
 */
@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapperDTO<T> {

    private String message;
    private Summary summary;
    private T data;

    public record Summary(long totalCount) {
    }

    public ResponseWrapperDTO(T data) {
        this.data = data;
    }

    public ResponseWrapperDTO(T data, Summary summary,String message) {
        this.data = data;
        this.summary = summary;
    }
}
