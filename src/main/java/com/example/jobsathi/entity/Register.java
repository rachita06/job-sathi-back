package com.example.jobsathi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */

@Entity
@Table(name = "register")
@Getter
@Setter
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String registeredId;
    @Column(name = "firstname", unique = true)
    private String firstName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "resumes")
    private List<ResumeEntity> resumes = new ArrayList<>();
}
