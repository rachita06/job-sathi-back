package com.example.jobsathi.dto.response;

import java.util.List;

/**
 * Created by Rabindra Adhikari on 3/20/26
 */
public record LoginResponseDTO(String accessToken, String type, String refreshToken, List<String> mode) {
}
