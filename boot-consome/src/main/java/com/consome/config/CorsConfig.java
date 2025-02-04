package com.consome.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로 허용
                .allowedOrigins("http://localhost:3000") // Vue.js 프론트엔드 (포트 3000) 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 허용할 HTTP 메서드
                .allowCredentials(true); // 인증 정보(쿠키 등) 포함 허용
    }
}
