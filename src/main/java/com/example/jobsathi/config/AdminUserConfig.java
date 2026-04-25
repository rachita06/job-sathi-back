package com.example.jobsathi.config;

import com.example.jobsathi.entity.Register;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Rabindra Adhikari on 4/25/26
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminUserConfig implements ApplicationRunner {

    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        createAdminIfNotExists();
    }

    private void createAdminIfNotExists() {
        String email = "admin@gmail.com";

        if (registerRepository.existsByEmail(email)) {
            LOGGER.info("Admin user already exists");
            return;
        }

        LOGGER.info("Creating default admin user");

        Register register = new Register();
        register.setEmail(email);
        register.setRole(Role.ADMIN.name());
        register.setPassword(passwordEncoder.encode("password"));

        registerRepository.save(register);
    }
}
