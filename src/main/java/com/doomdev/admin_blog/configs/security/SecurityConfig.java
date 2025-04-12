package com.doomdev.admin_blog.configs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AuthServiceContext authServiceContext;

    private final AuthConfig authConfig;

    public static final String[] WHITELIST_URLS = new String[] {
            "/*",
    };

    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public SecurityFilterChain httpPublic(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors((cors) -> cors.configurationSource(apiConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(WHITELIST_URLS)
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .sessionManagement(
                        configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public SecurityFilterChain httpAdmin(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors((cors) -> cors.configurationSource(apiConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(
                        new JWTRequestFilter(authServiceContext, authConfig),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
