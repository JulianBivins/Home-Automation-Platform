package com.homeautomation.homeAutomation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {


    @Autowired
    private  JwtAuthenticationFilterConfig jwtAuthFilter;
    @Autowired
    private  AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Whitelisting
//                        .requestMatchers("/auth/register", "/auth/authenticate").permitAll()
                        .requestMatchers("/users/**").hasAnyRole("ADMIN", "USER") // Securing other user paths
//                        .requestMatchers("/demo").hasAnyRole("ADMIN", "USER") // Securing other user paths
//                        .requestMatchers("/rules/**").authenticated() // Ensure rules endpoints require authentication
                        .requestMatchers("/rules/**").hasAnyRole("ADMIN", "USER") // Securing other user paths
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Enforcing stateless sessions (JWT)
                )
                .authenticationProvider(authenticationProvider) // Setting custom authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adding JWT filter

        return http.build();
    }

}
