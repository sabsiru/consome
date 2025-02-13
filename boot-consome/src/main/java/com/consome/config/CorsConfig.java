package com.consome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        String domain = "https://3d9a-118-91-7-41.ngrok-free.app/";
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", domain)); // ✅ Vue.js 주소 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ 허용할 HTTP 메서드
//        configuration.setAllowedHeaders(List.of("*")); // ✅ 모든 헤더 허용
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // ✅ 요청에서 허용할 헤더 지정
        configuration.setExposedHeaders(List.of("Authorization", "X-Auth-Token")); // ✅ 클라이언트가 읽을 수 있도록 허용할 응답 헤더 지정
        configuration.setAllowCredentials(true); // ✅ 쿠키 포함 허용 (필요할 경우)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // ✅ 모든 경로에 대해 적용

        return source;
    }
}
