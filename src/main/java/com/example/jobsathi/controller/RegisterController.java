package com.example.jobsathi.controller;

import com.example.jobsathi.dto.request.RegisterRequestDTO;
import com.example.jobsathi.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO requestDTO) {
        var response = registerService.registerUser(requestDTO);
        return ResponseEntity.ok(response);
    }
}
