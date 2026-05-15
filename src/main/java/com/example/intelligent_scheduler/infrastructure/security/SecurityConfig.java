package com.example.intelligent_scheduler.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        // Cấu hình bảo mật sẽ được thực hiện ở đây
        // Ví dụ: cấu hình JWT, OAuth2, hoặc các phương thức xác thực khác
    // cung cấp các bean liên quan đến bảo mật như AuthenticationManager, PasswordEncoder, v.v.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // tắt CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Tạm thời tắt CORS để dễ test từ Postman (sau này có Frontend sẽ bật lại)
                .cors(AbstractHttpConfigurer::disable)
                //  phân quyền các enpoint
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả mọi người truy cập vào các API đăng ký, đăng nhập
                        .requestMatchers("/api/v1/auth/**").permitAll() // Cho phép truy cập không cần xác thực vào các endpoint liên quan đến auth
                        .anyRequest().authenticated() // Yêu cầu xác thực cho tất cả các endpoint khác
                );
        return http.build();
    }
}
