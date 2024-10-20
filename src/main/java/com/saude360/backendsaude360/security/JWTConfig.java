package com.saude360.backendsaude360.security;


import com.saude360.backendsaude360.security.exceptionhandler.CustomAccessDeniedHandler;
import com.saude360.backendsaude360.security.exceptionhandler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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

@Configuration
@EnableWebSecurity
public class JWTConfig {

    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

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

    // Endpoints que requerem autenticação para serem acessados
    private static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
            "/healthSector/",
            "/transaction/",
            "/transaction/{id}",
            "/user/",
            "/user/patient/",
            "/user/patient/{id}",
            "/user/professional/",
            "/user/professional/{id}",
            "/consultation/",
            "/consultation/{id}/evolution-history",
            "/consultation/{id}",
            "/orientation/{id}",
            "/orientation/",
            "/orientation-responses/{orientationId}",
            "/orientation-responses/{patientId}",
            "/orientation-responses/"
    };

    // Endpoints que só podem ser acessador por usuários com permissão de profissional
    private static final String [] ENDPOINTS_PROFESSIONAL = {
            "/user/patient/",
            "/user/professional/",
            "/user/patient/professional",
            "/user/patient/consultation-and-orientation",
            "/consultation/patient/{patientId}",
            "/clinic/",
            "/clinic/{id}",
            "/orientation/patient/{patientId}"
    };

    // Endpoints que só podem ser acessador por usuários com permissão de paciente
    private static final String [] ENDPOINTS_PATIENT = {
            ""
    };

    private static final String [] ENDPOINTS_PUT_PATIENT = {
            "/user/patient/{id}"
    };

    private static final String [] ENDPOINTS_PUT_PROFESSIONAL = {
            "/user/professional/{id}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(csrf -> Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_POST_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.GET, ENDPOINTS_GET_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.PUT, ENDPOINTS_PUT_PATIENT).hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, ENDPOINTS_PUT_PROFESSIONAL).hasRole("PROFESSIONAL")
                        .requestMatchers(ENDPOINTS_PROFESSIONAL).hasRole("PROFESSIONAL")
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                        .anyRequest()
                        .denyAll())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception) -> exception
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
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
