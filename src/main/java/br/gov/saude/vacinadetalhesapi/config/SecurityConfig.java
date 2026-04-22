package br.gov.saude.vacinadetalhesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Mantém desativado para APIs REST
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                // Libera o acesso para o seu frontend (ajuste a porta se necessário)
                corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:8081"));
                corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                return corsConfiguration;
            }))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Garante que não peça senha
            );

        return http.build();
    }
}

