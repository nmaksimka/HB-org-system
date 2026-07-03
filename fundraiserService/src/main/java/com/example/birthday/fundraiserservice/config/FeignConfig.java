package com.example.birthday.fundraiserservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class FeignConfig {
    @Bean
    RequestInterceptor internalApiKey(@Value("${clients.internal-api-key}") String key) {
        return template -> template.header("X-Internal-Api-Key", key);
    }
}
