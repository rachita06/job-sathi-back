package com.example.jobsathi.controller;

import com.example.jobsathi.dto.request.LoginRequestDTO;
import com.example.jobsathi.dto.request.RegisterRequestDTO;
import com.example.jobsathi.dto.response.LoginResponseDTO;
import com.example.jobsathi.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO requestDTO) {
        var response = registerService.registerUser(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        return ResponseEntity.ok(registerService.login(requestDTO));
    }
}
