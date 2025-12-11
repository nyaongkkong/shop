package com.lym.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index.html", "/login", "/logout-success", "/signup",
                                "/css/**", "/js/**", "/images/**", "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")              // GET 로그인 페이지
                        .loginProcessingUrl("/login")     // POST 로그인 처리 URL (form action)
                        .usernameParameter("username")    // input name (기본값: username)
                        .passwordParameter("password")    // input name (기본값: password)
                        .defaultSuccessUrl("/", true)     // 로그인 성공 후 이동
                        .failureUrl("/login?error")       // 실패 시 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                   // 로그아웃 호출 URL (POST/GET 둘 다 가능하긴 한데 POST 권장)
                        .logoutSuccessUrl("/logout-success")    // 로그아웃 후 보여줄 페이지
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
