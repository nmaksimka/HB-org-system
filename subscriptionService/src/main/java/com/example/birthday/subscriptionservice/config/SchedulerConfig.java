package com.example.birthday.subscriptionservice.config;

import org.springframework.context.annotation.*;

import java.time.Clock;

@Configuration
public class SchedulerConfig {
    @Bean
    Clock reminderClock() {
        return Clock.systemUTC();
    }
}
