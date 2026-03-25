package com.example.jobsathi.controller;

import com.example.jobsathi.dto.response.UserResponseDTO;
import com.example.jobsathi.service.admin.AdminService;
import com.example.jobsathi.util.ResponseWrapperDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/user")
    ResponseEntity<ResponseWrapperDTO<List<UserResponseDTO>>> getUsers() {
        LOGGER.info("Started fetching all user");
        var users = adminService.getUsers();
        return ResponseEntity.ok(users);
    }
}
