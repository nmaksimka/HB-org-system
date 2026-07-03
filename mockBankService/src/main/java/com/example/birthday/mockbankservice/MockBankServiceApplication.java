package com.example.birthday.mockbankservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MockBankServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockBankServiceApplication.class, args);
    }
}
