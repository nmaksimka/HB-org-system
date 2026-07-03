package com.example.birthday.chatservice.config;
import feign.RequestInterceptor; import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*;
@Configuration public class FeignConfig{@Bean RequestInterceptor key(@Value("${clients.user-service.internal-api-key}")String key){return t->t.header("X-Internal-Api-Key",key);}}
