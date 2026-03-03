package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.request.LoginRequestDTO;
import com.example.jobsathi.dto.request.RegisterRequestDTO;
import com.example.jobsathi.entity.Register;
import com.example.jobsathi.exception.BadRequestException;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            return requestDTO.getEmail() +  " already exists";
        }
        registerRepository.save(register);
        return requestDTO.getEmail() + " successfully register";
    }

    @Override
    public String login(LoginRequestDTO requestDTO) {
        var registeredUser = registerRepository.getByEmail(requestDTO.getEmail());
        if (registeredUser.isPresent()) {
            LOGGER.info("User Exist");
            if (new BCryptPasswordEncoder().matches(requestDTO.getPassword(), registeredUser.get().getPassword())) {
                LOGGER.info("Login Successful");
                return "Login Successful";
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


}
