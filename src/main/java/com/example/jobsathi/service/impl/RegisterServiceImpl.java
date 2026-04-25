package com.example.jobsathi.service.impl;

import com.example.jobsathi.dto.request.LoginRequestDTO;
import com.example.jobsathi.dto.request.RegisterRequestDTO;
import com.example.jobsathi.dto.response.LoginResponseDTO;
import com.example.jobsathi.entity.Register;
import com.example.jobsathi.exception.BadRequestException;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.security.JwtTokenProvider;
import com.example.jobsathi.service.RegisterService;
import com.example.jobsathi.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

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
        register.setFirstName(requestDTO.getFirstName());
        register.setRegisteredId(UUID.randomUUID().toString());
        if (registerRepository.existsByEmail(requestDTO.getEmail())) {
            return requestDTO.getEmail() + " already exists";
        }
        registerRepository.save(register);
        return requestDTO.getEmail() + " successfully register";
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Register registeredUser = registerRepository.getByEmail(requestDTO.getEmail())
                .orElseThrow(() -> {
                    LOGGER.warn("Login attempt for unknown email: {}", requestDTO.getEmail());
                    return new BadRequestException("Invalid email or password");
                });
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registeredUser.getEmail(), requestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            throw new BadRequestException("User not found");
        }
        String token = tokenProvider.generateToken(userDetails);
        return new LoginResponseDTO(token, "Bearer", null, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }


}
