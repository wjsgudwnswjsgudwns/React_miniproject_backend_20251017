package com.jhj.miniproject.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@EnableWebSecurity
public class SecurityConfig {
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // ✅ 정적 리소스 먼저 허용 (가장 중요!)
                .requestMatchers(
                		"/", 
                	    "/index.html",
                	    "/static/**",
                	    "/favicon.ico",
                	    "/manifest.json",
                	    "/logo*.png",
                	    "/*.js",
                	    "/*.css",
                	    "/asset-manifest.json",
                	    "/**"
                ).permitAll()
                
                // 회원가입, 로그인
                .requestMatchers(HttpMethod.POST, "/api/auth/signup", "/api/auth/login").permitAll()
                .requestMatchers("/api/auth/logout", "/api/auth/me").permitAll()
                
                // 주문 관련
                .requestMatchers("/api/orders/**", "/api/orders/myorder/**").permitAll()
                
                // React 라우팅 경로
                .requestMatchers("/login", "/signup", "/board/**", "/tactics/**", "/buy", "/payment", "/myorder").permitAll()
                
                // 게시판 읽기 API
                .requestMatchers(HttpMethod.GET, "/api/board", "/api/board/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tactics", "/api/tactics/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comment/**").permitAll()
                
                // 게시판 쓰기/수정/삭제 API는 인증 필요
                .requestMatchers(HttpMethod.POST, "/api/board", "/api/tactics").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/board/**", "/api/tactics/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/board/**", "/api/tactics/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/comment/**").authenticated()
                
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/api/auth/login")
                .usernameParameter("userId")
                .passwordParameter("password")
                .successHandler((req, res, auth) -> {
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.setContentType("application/json");
                })
                .failureHandler((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                })
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> {
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.setContentType("application/json");
                })
            )
            // 인증 실패 시 401 반환 (리다이렉트 방지)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) -> {
                    // ✅ 정적 리소스 요청이면 그냥 통과
                    String requestURI = req.getRequestURI();
                    if (requestURI.startsWith("/static/") || 
                        requestURI.endsWith(".js") || 
                        requestURI.endsWith(".css") ||
                        requestURI.endsWith(".html") ||
                        requestURI.equals("/") ||
                        requestURI.equals("/favicon.ico")) {
                        return;
                    }
                    
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\": \"Unauthorized\"}");
                })
            );

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8888", "http://172.30.1.24:3000", "http://172.30.1.24:8888"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}