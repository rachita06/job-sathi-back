package com.example.jobsathi.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GreetingController {

    @GetMapping("/greeting")
    public String getGreeting() {
        return "This is resume analyzer powered by AI";
    }

    @GetMapping("/get-time")
    public LocalTime getTime() {
        return LocalTime.now();
    }

    @GetMapping("/get-date")
    public LocalDate getDate() {
        return LocalDate.now();
    }

    @GetMapping("/get-found-by")
    public String getFounderName() {
        return "Rachita Adhikari";
    }

}
