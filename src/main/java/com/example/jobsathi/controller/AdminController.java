package com.example.jobsathi.controller;

import com.example.jobsathi.dto.response.UserResponseDTO;
import com.example.jobsathi.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rabindra Adhikari on 3/9/26
 */

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final RegisterRepository registerRepository;

    @GetMapping("/user")
    ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = registerRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
