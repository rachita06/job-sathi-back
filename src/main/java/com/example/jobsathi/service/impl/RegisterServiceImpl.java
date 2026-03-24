package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.request.LoginRequestDTO;
import com.example.jobsathi.dto.request.RegisterRequestDTO;
import com.example.jobsathi.dto.response.LoginResponseDTO;
import com.example.jobsathi.entity.Register;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.security.JwtTokenProvider;
import com.example.jobsathi.service.RegisterService;
import com.example.jobsathi.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceImpl implements RegisterService {
    private final RegisterRepository registerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public String registerUser(RegisterRequestDTO requestDTO) {
        Register register = new Register();
        register.setEmail(requestDTO.getEmail());
        register.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        register.setRole(Role.USER.name());
        if (registerRepository.existsByEmail(requestDTO.getEmail())) {
            return requestDTO.getEmail() + " already exists";
        }
        registerRepository.save(register);
        return requestDTO.getEmail() + " successfully register";
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails);
        return new LoginResponseDTO(token, "Bearer", null);
    }


}
