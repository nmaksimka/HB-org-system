package com.example.birthday.giftservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class FeignConfig {
    @Bean
    RequestInterceptor internalApiKey(
            @Value("${clients.user-service.internal-api-key}") String key) {
        return template -> template.header("X-Internal-Api-Key", key);
    }
}
