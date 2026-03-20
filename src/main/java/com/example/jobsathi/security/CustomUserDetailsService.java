package com.example.jobsathi.security;

import com.example.jobsathi.entity.Register;
import com.example.jobsathi.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rabindra Adhikari on 3/20/26
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final RegisterRepository userRepository;

    @NotNull
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Register register = userRepository.getByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + register.getRole()));

        return new CustomUserDetails(
                register.getEmail(),
                register.getPassword(),
                authorities
        );
    }
}
