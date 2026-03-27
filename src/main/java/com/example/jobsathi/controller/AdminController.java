package com.example.jobsathi.controller;

import com.example.jobsathi.dto.response.AdminResumeSummaryResponseDTO;
import com.example.jobsathi.dto.response.UserResponseDTO;
import com.example.jobsathi.service.admin.AdminService;
import com.example.jobsathi.util.ResponseWrapperDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Rabindra Adhikari on 3/9/26
 */

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
  public  ResponseEntity<ResponseWrapperDTO<List<UserResponseDTO>>> getUsers() {
        LOGGER.info("Started fetching all user");
        var users = adminService.getUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<ResponseWrapperDTO<UserResponseDTO>> getUser(@PathVariable String username) {
        LOGGER.info("Started fetching user by {}", username);
        ResponseWrapperDTO<UserResponseDTO> user = adminService.getUser(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/summary")
    public ResponseEntity<AdminResumeSummaryResponseDTO> getSummary() {
        LOGGER.info("Started Getting Admin Summary for user");
        AdminResumeSummaryResponseDTO summary = adminService.getUserSummary();
        return ResponseEntity.ok(summary);
    }

}
