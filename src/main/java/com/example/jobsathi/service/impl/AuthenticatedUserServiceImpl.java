package com.example.jobsathi.service.impl;

import com.example.jobsathi.entity.Register;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.service.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Created by Rabindra Adhikari on 3/22/26
 */
@Service
@RequiredArgsConstructor
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
    private final RegisterRepository registerRepository;
    @Override
    public Register getLoggedInUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // 1. Check authentication is null
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        // 2. Check principal is null
        Object principal = authentication.getPrincipal();
        if (principal == null || principal.equals("anonymousUser")) {
            throw new RuntimeException("Anonymous user not allowed");
        }

        // 3. Cast to UserDetails
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();

        // 4. Get user from DB
        return registerRepository.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
