package com.example.jobsathi.controller;

import com.example.jobsathi.dto.GrammarIssue;
import com.example.jobsathi.dto.response.AiAnalysisResumeResponseDTO;
import com.example.jobsathi.service.AIService;
import com.example.jobsathi.service.DocumentExtractService;
import com.example.jobsathi.service.GrammarCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by Rabindra Adhikari on 2/25/26
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-sathi")
public class PDFExtractController {
    private final GrammarCheckService  grammarCheckService;
    private final DocumentExtractService extractContent;
    private final AIService aiService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/upload-pdf")
    @ResponseStatus(OK)
    public ResponseEntity<?> classify(@RequestParam("file") final MultipartFile pdfFile) {
        return ResponseEntity.ok().body(extractContent.extractText(pdfFile));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/check-grammar")
    @ResponseStatus(OK)
    public ResponseEntity<List<GrammarIssue>> checkGrammar(@RequestParam("file") final MultipartFile pdfFile) {
        return ResponseEntity.ok().body(grammarCheckService.checkGrammar(extractContent.extractText(pdfFile)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/analysis")
    @ResponseStatus(OK)
    public ResponseEntity<AiAnalysisResumeResponseDTO> aiResumeAnalysis(@RequestParam("file") final MultipartFile pdfFile) {
        return ResponseEntity.ok().body(aiService.analysis(extractContent.extractText(pdfFile)));
    }


}


