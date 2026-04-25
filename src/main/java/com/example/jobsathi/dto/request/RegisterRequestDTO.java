package com.example.jobsathi.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
@Getter
@Setter
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String firstName;
}
