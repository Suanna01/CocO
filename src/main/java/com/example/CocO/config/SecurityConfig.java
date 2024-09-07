package com.example.CocO.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/google", "/auth/kakao", "/auth/naver").permitAll() // 소셜 로그인 엔드포인트 접근 허용
                                .anyRequest().authenticated() // 나머지 요청은 인증을 요구합니다.
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 로그인 페이지 URL 설정
                                .permitAll() // 로그인 페이지는 모든 사용자에게 허용됩니다.
                )
                .csrf(csrf ->
                        csrf.disable() // CSRF 보호 비활성화 (개발 중에만 사용)
                );

        return http.build();
    }
}
