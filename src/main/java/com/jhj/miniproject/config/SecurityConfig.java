package com.jhj.miniproject.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity // 모든 요청이 url이 스프링 시큐리티의 제어를 받도록하는 annotation
public class SecurityConfig {
	
	 // 비밀번호 암호화용 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 필터 체인 설정 (Spring Boot 3.x / Security 6.x 방식)
    @Bean
       public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
           http
               .csrf(csrf -> csrf.disable())  //새 방식 (람다 DSL)
               .cors(Customizer.withDefaults())
               .authorizeHttpRequests(auth -> auth
                     .requestMatchers(
                           "/", 
                           "/index.html", 
                           "/login", 
                           "/signup", 
                           "/board/**", 
                           "/static/**")
                     .permitAll()                   
                       // 읽기 API는 로그인 없이 허용
                       .requestMatchers(
                             "/api/board", 
                             "/api/board/**", 
                             "/api/comments", 
                             "/api/comments/**")
                       .permitAll()
                       
                       // 쓰기/수정/삭제 API는 인증 필요
                       .requestMatchers(
                             "/api/board/write", 
                             "/api/board/update/**", 
                             "/api/board/delete/**")
                       .authenticated()
                       .requestMatchers(
                             "/api/comments/write", 
                             "/api/comments/delete/**")
                       .authenticated()           
                   .anyRequest().authenticated()
               )         
               .formLogin(login -> login    
                  .loginPage("/login").permitAll()   
                   .loginProcessingUrl("/api/auth/login")
                   .usernameParameter("userId")
                   .passwordParameter("password")
                   .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
                   .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
               )
               .logout(logout -> logout
                   .logoutUrl("/api/auth/logout")
                   .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
               ); 
          

           return http.build();
       }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React 개발 서버
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 쿠키, 세션 허용 시 필요

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
   

}