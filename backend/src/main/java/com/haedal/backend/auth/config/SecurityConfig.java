package com.haedal.backend.auth.config;

import com.haedal.backend.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@Configuration
@EnableWebSecurity // 웹사이트에 인증 필요기능 생성하는 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {



    @Value("${jwt.token.secret}")
    private String secretKey;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
        http
                .cors() // CORS 설정 활성화
                .and()
                .httpBasic().disable() //기본인증 사용 x
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)

//                .headers().frameOptions().disable()
                               //임시로 전체 허용
        .build();
    }

    //TODO : CORS 에러 때문에 생성, 공부 후 리팩토링
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 출처
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드
        configuration.setAllowCredentials(true); // credentials 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


