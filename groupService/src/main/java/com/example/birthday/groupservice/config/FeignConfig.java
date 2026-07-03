package com.example.birthday.groupservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    RequestInterceptor internalApiKeyInterceptor(
            @Value("${clients.user-service.internal-api-key}") String key) {
        return template -> template.header("X-Internal-Api-Key", key);
    }
}
