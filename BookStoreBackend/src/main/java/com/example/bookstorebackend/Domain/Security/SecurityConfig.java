package com.example.bookstorebackend.Domain.Security;

import com.example.bookstorebackend.Domain.Security.JWTAuth.JwtAuthenticationFilter;
import com.example.bookstorebackend.Domain.Security.JWTAuth.JwtService;
import com.example.bookstorebackend.Domain.UserService.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeRequests(authorizeRequests -> {
                    try {

                        authorizeRequests
                                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
                                .antMatchers("/api/auth/**", "/api/user/**", "/api/shopping-cart/**", "/api/client/**")
                                .permitAll()
                                .anyRequest().authenticated();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .cors(Customizer.withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
