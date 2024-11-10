package com.saude360.backendsaude360.security;


import com.saude360.backendsaude360.security.exceptionhandler.CustomAccessDeniedHandler;
import com.saude360.backendsaude360.security.exceptionhandler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class JWTConfig {

    private final JWTAuthenticationFilter filter;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Value("${frontend.url}")
    private String frontendUrl;

    public JWTConfig(JWTAuthenticationFilter filter, CustomAccessDeniedHandler accessDeniedHandler) {
        this.filter = filter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    private static final String [] ENDPOINTS_POST_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/users/login",
            "/user/professional/",
            "/api/authentication/login",
            "/healthSector/",
            "/user/forget-password/{cpf}",
            "/user/reset-password",
    };

    private static final String [] ENDPOINTS_GET_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/healthSector/",
    };

    private static final String [] ENDPOINTS_PUT_PROFESSIONAL = {
            "/user/professional/",
            "/transaction/{id}",
            "/professional/",
            "/consultation/{id}",
            "/clinic/{id}"
    };

    protected static final String [] ENDPOINTS_GET_PROFESSIONAL = {
            "/transaction/",
            "/transaction/{id}",
            "/user/patient/professional",
            "/user/patient/consultation-and-orientation",
            "/user/patient/{id}",
            "/consultation/",
            "/consultation/patient/{patientId}"
    };

    protected static final String [] ENDPOINTS_DELETE_PROFESSIONAL = {
            "/transaction/{id}",
            "/user/professional/{id}",
            "/orientation/{id}",
            "/consultation/{id}",
            "/clinic/{id}"
    };

    protected static final String [] ENDPOINTS_POST_PROFESSIONAL = {
            "/transaction/",
            "/user/professional/",
            "/user/patient/",
            "/orientation/patient/{patientId}",
            "/consultation/{id}",
            "/consultation/{consultationId}/evolution-history",
            "/clinic/"
    };

    protected static final String [] ENDPOINTS_GET_USER = {
            "/user/",
            "/user/{id}",
            "/user/{cpf}",
            "/user/professional/",
            "/user/professional/{id}",
            "/user/professional/",
            "/user/patient/",
            "/user/patient/{id}",
            "/orientation-responses/{patientId}",
            "/orientation-responses/",
            "/orientation/",
            "/orientation/{id}",
            "/consultation/",
            "/consultation/{id}",
            "/clinic/",
            "/clinic/{id}",
    };

    protected static final String [] ENDPOINTS_POST_USER = {
            "/orientation-responses/{orientationId}",
    };

    protected static final String [] ENDPOINTS_PUT_USER = {
            ""
    };

    protected static final String [] ENDPOINTS_DELETE_USER = {
            ""
    };

    private static final String [] ENDPOINTS_GET_PATIENT = {
            "/consultation/patient/"
    };

    private static final String [] ENDPOINTS_PUT_PATIENT = {
            "/user/patient/"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> {
                    CorsConfigurationSource source = corsConfigurationSource();
                    cors.configurationSource(source);
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_POST_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_GET_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.PUT, ENDPOINTS_PUT_PATIENT).hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_GET_PATIENT).hasRole("PATIENT")
                        .requestMatchers(HttpMethod.DELETE, ENDPOINTS_DELETE_PROFESSIONAL).hasRole("PROFESSIONAL")
                        .requestMatchers(HttpMethod.PUT, ENDPOINTS_PUT_PROFESSIONAL).hasRole("PROFESSIONAL")
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_GET_PROFESSIONAL).hasRole("PROFESSIONAL")
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_POST_PROFESSIONAL).hasRole("PROFESSIONAL")
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_GET_USER).authenticated()
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_POST_USER).authenticated()
                        .anyRequest()
                        .denyAll())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new UnauthorizedHandler())
                        .accessDeniedHandler(accessDeniedHandler))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
