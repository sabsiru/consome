package com.consome.auth;

import com.consome.config.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private final CorsConfig corsConfig;
    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // ✅ CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화 (JWT 사용 시 필요)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ JWT 기반 인증을 위해 세션 사용 안 함
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**", "/error","/").permitAll() // ✅ "/user/**" 및 "/error" 엔드포인트는 인증 없이 허용
                        .anyRequest().authenticated() // ✅ 그 외 모든 요청은 인증 필요
                )
                .formLogin(login -> login.disable()) // ✅ 폼 로그인 비활성화 (JWT 사용 시 필요)
                .httpBasic(httpBasic -> httpBasic.disable()); // ✅ HTTP Basic 인증 비활성화 (JWT 사용 시 필요)

        return http.build();
    }

}
