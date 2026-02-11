package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.request.RegisterRequestDTO;
import com.example.jobsathi.entity.Register;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceImpl implements RegisterService {
    private final RegisterRepository registerRepository;

    @Override
    public String registerUser(RegisterRequestDTO requestDTO) {
        Register register =new Register();
        register.setEmail(requestDTO.getEmail());
        register.setPassword(new BCryptPasswordEncoder().encode(requestDTO.getPassword()));
        if (registerRepository.existsByEmail(requestDTO.getEmail())){
            return requestDTO.getEmail() +  "already exists";
        }
        registerRepository.save(register);
        return requestDTO.getEmail() + "successfully register";
    }

}
