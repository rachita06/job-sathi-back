package com.example.jobsathi.controller;

import com.example.jobsathi.dto.response.ChatResponseDTO;
import com.example.jobsathi.dto.response.ResumeScoreResponseDTO;
import com.example.jobsathi.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Rabindra Adhikari on 2/25/26
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-sathi")
public class PDFExtractController {
    private final AIService aiService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/analysis")
    public ResponseEntity<ResumeScoreResponseDTO> resumeAnalysisTfIDFAlgo(@RequestParam("file") final MultipartFile pdfFile) {
        return ResponseEntity.ok().body(aiService.resumeAnalysis(pdfFile));
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(@RequestParam String chat) {
        return ResponseEntity.ok(aiService.simpleChat(chat));
    }

}


