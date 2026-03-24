package com.example.jobsathi.repository;

import com.example.jobsathi.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Rabindra Adhikari on 3/20/26
 */
public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    List<ResumeEntity> findByUserId(Long userId); 
}
