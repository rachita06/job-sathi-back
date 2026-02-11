package com.example.jobsathi.repository;

import com.example.jobsathi.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
public interface RegisterRepository extends JpaRepository<Register, Long> {
    boolean existsByEmail(String email);
}
