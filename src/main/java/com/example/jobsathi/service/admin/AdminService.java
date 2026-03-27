package com.example.jobsathi.service.admin;

import com.example.jobsathi.dto.response.UserResponseDTO;
import com.example.jobsathi.util.ResponseWrapperDTO;

import java.util.List;

/**
 * Created by Rabindra Adhikari on 3/25/26
 */
public interface AdminService {
    ResponseWrapperDTO<List<UserResponseDTO>> getUsers();

    ResponseWrapperDTO<UserResponseDTO> getUser(String username);
}
