package com.example.jobsathi.service;

import com.example.jobsathi.dto.request.LoginRequestDTO;
import com.example.jobsathi.dto.request.RegisterRequestDTO;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
public interface RegisterService {
    String registerUser(RegisterRequestDTO requestDTO);

    String login(LoginRequestDTO requestDTO);
}
