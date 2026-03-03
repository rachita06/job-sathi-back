package com.example.jobsathi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "password")
    private String password;

}
