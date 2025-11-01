package br.com.fiap.cinefinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers(HttpMethod.GET, "/users").authenticated()

                        .anyRequest().permitAll())
                        .csrf(csrf -> csrf.disable());


        // // Disable CSRF and frame options to enable H2 console access
        // .csrf(csrf -> csrf.disable())
        // .headers(headers -> headers
        // .frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
